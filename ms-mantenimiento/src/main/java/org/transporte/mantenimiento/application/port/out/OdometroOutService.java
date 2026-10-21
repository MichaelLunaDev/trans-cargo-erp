package org.transporte.mantenimiento.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.mantenimiento.domian.bean.Odometro;

public interface OdometroOutService {

    Odometro guardar(Odometro odometro);

    Optional<Odometro> buscarPorVehiculoId(Long vehiculoId);

    List<Odometro> listarTodos();
}
