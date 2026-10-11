package org.transporte.mantenimiento.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.mantenimiento.application.port.in.NeumaticoUseCase;
import org.transporte.mantenimiento.application.port.out.NeumaticoOutService;
import org.transporte.mantenimiento.domian.bean.Neumatico;
import org.transporte.mantenimiento.domian.constraints.NeumaticoConstraint;
import org.transporte.mantenimiento.domian.model.RecursoDuplicadoException;
import org.transporte.mantenimiento.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NeumaticoService implements NeumaticoUseCase {

    private final NeumaticoOutService neumaticoOutService;
    private final NeumaticoConstraint neumaticoConstraint;

    @Override
    @Transactional
    public Neumatico registrar(Neumatico neumatico) {
        neumatico.setPosicion(neumatico.getPosicion().toUpperCase());
        validarPosicionLibre(null, neumatico.getVehiculoId(), neumatico.getPosicion());

        neumatico.setId(null);
        neumatico.setKmRecorridos(0.0);
        neumatico.setEstado(neumaticoConstraint.determinarEstado(0.0, neumatico.getVidaUtilKm()));
        return neumaticoOutService.guardar(neumatico);
    }

    @Override
    @Transactional(readOnly = true)
    public Neumatico buscarPorId(Long id) {
        return neumaticoOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el neumatico con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Neumatico> listarPorVehiculo(Long vehiculoId) {
        return neumaticoOutService.listarPorVehiculo(vehiculoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Neumatico> listarTodos() {
        return neumaticoOutService.listarTodos();
    }

    @Override
    @Transactional
    public Neumatico actualizar(Long id, Neumatico neumatico) {
        Neumatico actual = buscarPorId(id);
        String posicion = neumatico.getPosicion().toUpperCase();
        validarPosicionLibre(id, neumatico.getVehiculoId(), posicion);

        actual.setVehiculoId(neumatico.getVehiculoId());
        actual.setPosicion(posicion);
        actual.setVidaUtilKm(neumatico.getVidaUtilKm());
        actual.setEstado(neumaticoConstraint.determinarEstado(actual.getKmRecorridos(), actual.getVidaUtilKm()));
        return neumaticoOutService.guardar(actual);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        buscarPorId(id);
        neumaticoOutService.eliminar(id);
    }

    @Override
    @Transactional
    public Neumatico cambiar(Long id) {
        Neumatico neumatico = buscarPorId(id);
        neumatico.setKmRecorridos(0.0);
        neumatico.setEstado(neumaticoConstraint.determinarEstado(0.0, neumatico.getVidaUtilKm()));
        return neumaticoOutService.guardar(neumatico);
    }

    private void validarPosicionLibre(Long id, Long vehiculoId, String posicion) {
        neumaticoOutService.buscarPorVehiculoYPosicion(vehiculoId, posicion)
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException("El vehiculo " + vehiculoId
                            + " ya tiene un neumatico en la posicion " + posicion);
                });
    }
}
