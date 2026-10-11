package org.transporte.mantenimiento.application.port.in;

import java.util.List;

import org.transporte.mantenimiento.domian.bean.Neumatico;

public interface NeumaticoUseCase {

    Neumatico registrar(Neumatico neumatico);

    Neumatico buscarPorId(Long id);

    List<Neumatico> listarPorVehiculo(Long vehiculoId);

    List<Neumatico> listarTodos();

    Neumatico actualizar(Long id, Neumatico neumatico);

    void eliminar(Long id);

    Neumatico cambiar(Long id);
}
