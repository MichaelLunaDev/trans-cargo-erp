package org.transporte.mantenimiento.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.mantenimiento.application.port.in.OdometroUseCase;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.domian.bean.Odometro;
import org.transporte.mantenimiento.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OdometroService implements OdometroUseCase {

    private final OdometroOutService odometroOutService;

    @Override
    @Transactional(readOnly = true)
    public Odometro buscarPorVehiculoId(Long vehiculoId) {
        return odometroOutService.buscarPorVehiculoId(vehiculoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe odometro registrado para el vehiculo " + vehiculoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Odometro> listarTodos() {
        return odometroOutService.listarTodos();
    }
}
