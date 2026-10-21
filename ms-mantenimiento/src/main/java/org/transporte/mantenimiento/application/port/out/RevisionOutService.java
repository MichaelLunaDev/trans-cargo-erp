package org.transporte.mantenimiento.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;

public interface RevisionOutService {

    Revision guardar(Revision revision);

    Optional<Revision> buscarPorId(Long id);

    List<Revision> listarPorVehiculo(Long vehiculoId);

    List<Revision> listarPorVehiculoYEstado(Long vehiculoId, EstadoRevision estado);

    List<Revision> listarPorEstado(EstadoRevision estado);

    void eliminar(Long id);
}
