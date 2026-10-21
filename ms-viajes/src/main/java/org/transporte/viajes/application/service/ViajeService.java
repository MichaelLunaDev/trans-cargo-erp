package org.transporte.viajes.application.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.viajes.application.port.in.ViajeUseCase;
import org.transporte.viajes.application.port.out.ConductorOutService;
import org.transporte.viajes.application.port.out.VehiculoOutService;
import org.transporte.viajes.application.port.out.ViajeEventoOutService;
import org.transporte.viajes.application.port.out.ViajeOutService;
import org.transporte.viajes.domian.bean.ConductorInfo;
import org.transporte.viajes.domian.bean.VehiculoInfo;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.bean.ViajeIniciado;
import org.transporte.viajes.domian.constraints.ConductorConstraint;
import org.transporte.viajes.domian.constraints.PlacaConstraint;
import org.transporte.viajes.domian.constraints.VehiculoConstraint;
import org.transporte.viajes.domian.constraints.ViajeConstraint;
import org.transporte.viajes.domian.enums.EstadoViaje;
import org.transporte.viajes.domian.model.RecursoDuplicadoException;
import org.transporte.viajes.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViajeService implements ViajeUseCase {

    private static final List<EstadoViaje> ESTADOS_ACTIVOS = List.of(EstadoViaje.PROGRAMADO, EstadoViaje.EN_CURSO);

    private final ViajeOutService viajeOutService;
    private final VehiculoOutService vehiculoOutService;
    private final ConductorOutService conductorOutService;
    private final ViajeEventoOutService viajeEventoOutService;
    private final ViajeConstraint viajeConstraint;
    private final VehiculoConstraint vehiculoConstraint;
    private final ConductorConstraint conductorConstraint;
    private final PlacaConstraint placaConstraint;

    @Override
    public Viaje programar(Viaje viaje) {
        viajeConstraint.validarDatos(viaje);

        VehiculoInfo vehiculo = vehiculoOutService.buscarPorId(viaje.getVehiculoId());
        vehiculoConstraint.validarApto(vehiculo, viaje.getFechaSalida());
        String placa = vehiculo.getPlaca() == null ? null : vehiculo.getPlaca().toUpperCase();
        placaConstraint.validarPlaca(placa);

        ConductorInfo conductor = conductorOutService.buscarPorId(viaje.getConductorId());
        conductorConstraint.validarApto(conductor, viaje.getFechaSalida());

        if (viajeOutService.existePorVehiculoYFechaSalida(viaje.getVehiculoId(), viaje.getFechaSalida(),
                ESTADOS_ACTIVOS)) {
            throw new RecursoDuplicadoException("El vehiculo con id " + viaje.getVehiculoId()
                    + " ya tiene un viaje programado para " + viaje.getFechaSalida());
        }
        if (viajeOutService.existePorConductorYFechaSalida(viaje.getConductorId(), viaje.getFechaSalida(),
                ESTADOS_ACTIVOS)) {
            throw new RecursoDuplicadoException("El conductor con id " + viaje.getConductorId()
                    + " ya tiene un viaje programado para " + viaje.getFechaSalida());
        }

        viaje.setId(null);
        viaje.setPlaca(placa);
        viaje.setOrigen(viaje.getOrigen().trim());
        viaje.setDestino(viaje.getDestino().trim());
        viaje.setFechaLlegada(null);
        viaje.setEstado(EstadoViaje.PROGRAMADO);
        return viajeOutService.guardar(viaje);
    }

    @Override
    public Viaje iniciar(Long id) {
        Viaje viaje = obtener(id);
        viajeConstraint.validarTransicion(viaje, EstadoViaje.EN_CURSO);
        viajeConstraint.validarSinViajeEnCurso(viaje,
                viajeOutService.listarPorVehiculoYEstado(viaje.getVehiculoId(), EstadoViaje.EN_CURSO),
                viajeOutService.listarPorConductorYEstado(viaje.getConductorId(), EstadoViaje.EN_CURSO));

        viaje.setEstado(EstadoViaje.EN_CURSO);
        Viaje guardado = viajeOutService.guardar(viaje);

        ViajeIniciado evento = new ViajeIniciado(UUID.randomUUID().toString(), guardado.getId(),
                guardado.getVehiculoId(), guardado.getPlaca(), guardado.getDistanciaKm(),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        try {
            viajeEventoOutService.publicarViajeIniciado(evento);
            log.info("Evento viaje.iniciado publicado: eventId={}, viajeId={}", evento.getEventId(), guardado.getId());
        } catch (RuntimeException ex) {
            log.error("No se pudo publicar el evento viaje.iniciado (eventId={}, viajeId={}, vehiculoId={}, "
                    + "distanciaKm={}). MS-Mantenimiento no sumara estos km. Causa: {}",
                    evento.getEventId(), evento.getViajeId(), evento.getVehiculoId(), evento.getDistanciaKm(),
                    ex.getMessage());
        }
        return guardado;
    }

    @Override
    @Transactional
    public Viaje finalizar(Long id) {
        Viaje viaje = obtener(id);
        viajeConstraint.validarTransicion(viaje, EstadoViaje.FINALIZADO);
        viaje.setEstado(EstadoViaje.FINALIZADO);
        viaje.setFechaLlegada(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return viajeOutService.guardar(viaje);
    }

    @Override
    @Transactional
    public Viaje cancelar(Long id) {
        Viaje viaje = obtener(id);
        viajeConstraint.validarTransicion(viaje, EstadoViaje.CANCELADO);
        viaje.setEstado(EstadoViaje.CANCELADO);
        return viajeOutService.guardar(viaje);
    }

    @Override
    @Transactional(readOnly = true)
    public Viaje buscarPorId(Long id) {
        return obtener(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Viaje> listar(EstadoViaje estado, Long vehiculoId) {
        return viajeOutService.listar(estado, vehiculoId);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Viaje viaje = obtener(id);
        viajeConstraint.validarEliminacion(viaje);
        viajeOutService.eliminar(id);
    }

    private Viaje obtener(Long id) {
        return viajeOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el viaje con id " + id));
    }
}
