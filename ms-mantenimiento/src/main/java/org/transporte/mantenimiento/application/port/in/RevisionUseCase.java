package org.transporte.mantenimiento.application.port.in;

import java.util.List;

import org.transporte.mantenimiento.domian.bean.Revision;

public interface RevisionUseCase {

    Revision programar(Revision revision);

    Revision marcarRealizada(Long id);

    List<Revision> listarPorVehiculo(Long vehiculoId);

    List<Revision> listarVencidas();
}
