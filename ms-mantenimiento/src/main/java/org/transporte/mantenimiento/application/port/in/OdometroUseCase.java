package org.transporte.mantenimiento.application.port.in;

import java.util.List;

import org.transporte.mantenimiento.domian.bean.Odometro;

public interface OdometroUseCase {

    Odometro buscarPorVehiculoId(Long vehiculoId);

    List<Odometro> listarTodos();
}
