package org.transporte.mantenimiento.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.mantenimiento.application.port.in.RegistrarViajeUseCase;
import org.transporte.mantenimiento.application.port.out.EventoProcesadoOutService;
import org.transporte.mantenimiento.application.port.out.NeumaticoOutService;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.application.port.out.RevisionOutService;
import org.transporte.mantenimiento.domian.bean.Neumatico;
import org.transporte.mantenimiento.domian.bean.Odometro;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.bean.ViajeIniciado;
import org.transporte.mantenimiento.domian.constraints.NeumaticoConstraint;
import org.transporte.mantenimiento.domian.constraints.OdometroConstraint;
import org.transporte.mantenimiento.domian.constraints.PlacaConstraint;
import org.transporte.mantenimiento.domian.constraints.RevisionConstraint;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarViajeService implements RegistrarViajeUseCase {

    private final OdometroOutService odometroOutService;
    private final NeumaticoOutService neumaticoOutService;
    private final RevisionOutService revisionOutService;
    private final EventoProcesadoOutService eventoProcesadoOutService;
    private final OdometroConstraint odometroConstraint;
    private final NeumaticoConstraint neumaticoConstraint;
    private final RevisionConstraint revisionConstraint;
    private final PlacaConstraint placaConstraint;

    @Override
    @Transactional
    public void registrar(ViajeIniciado viaje) {
        if (eventoProcesadoOutService.existe(viaje.getEventId())) {
            log.info("Evento {} ya fue procesado, se ignora", viaje.getEventId());
            return;
        }

        String placa = viaje.getPlaca() == null ? null : viaje.getPlaca().toUpperCase();
        placaConstraint.validarPlaca(placa);
        odometroConstraint.validarDistancia(viaje.getDistanciaKm());

        Odometro odometro = actualizarOdometro(viaje, placa);
        actualizarNeumaticos(viaje.getVehiculoId(), viaje.getDistanciaKm());
        marcarRevisionesVencidas(viaje.getVehiculoId(), odometro.getKilometrajeAcumulado());

        eventoProcesadoOutService.registrar(viaje.getEventId());
        log.info("Viaje {} registrado: vehiculo {} suma {} km (acumulado {} km)",
                viaje.getViajeId(), viaje.getVehiculoId(), viaje.getDistanciaKm(),
                odometro.getKilometrajeAcumulado());
    }

    private Odometro actualizarOdometro(ViajeIniciado viaje, String placa) {
        Odometro odometro = odometroOutService.buscarPorVehiculoId(viaje.getVehiculoId())
                .orElse(new Odometro(null, viaje.getVehiculoId(), placa, 0.0, null));
        odometro.setPlaca(placa);
        odometro.setKilometrajeAcumulado(
                odometroConstraint.sumarKilometraje(odometro.getKilometrajeAcumulado(), viaje.getDistanciaKm()));
        odometro.setFechaActualizacion(viaje.getFecha() == null ? LocalDateTime.now() : viaje.getFecha());
        return odometroOutService.guardar(odometro);
    }

    private void actualizarNeumaticos(Long vehiculoId, Double distanciaKm) {
        for (Neumatico neumatico : neumaticoOutService.listarPorVehiculo(vehiculoId)) {
            neumatico.setKmRecorridos(neumatico.getKmRecorridos() + distanciaKm);
            neumatico.setEstado(neumaticoConstraint.determinarEstado(
                    neumatico.getKmRecorridos(), neumatico.getVidaUtilKm()));
            neumaticoOutService.guardar(neumatico);
        }
    }

    private void marcarRevisionesVencidas(Long vehiculoId, Double kilometrajeAcumulado) {
        for (Revision revision : revisionOutService.listarPorVehiculoYEstado(vehiculoId, EstadoRevision.PENDIENTE)) {
            if (revisionConstraint.estaVencida(revision, kilometrajeAcumulado)) {
                revision.setEstado(EstadoRevision.VENCIDA);
                revisionOutService.guardar(revision);
            }
        }
    }
}
