package org.transporte.mantenimiento.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.mantenimiento.domian.bean.Neumatico;

public interface NeumaticoOutService {

    Neumatico guardar(Neumatico neumatico);

    Optional<Neumatico> buscarPorId(Long id);

    Optional<Neumatico> buscarPorVehiculoYPosicion(Long vehiculoId, String posicion);

    List<Neumatico> listarPorVehiculo(Long vehiculoId);

    List<Neumatico> listarTodos();

    void eliminar(Long id);
}
