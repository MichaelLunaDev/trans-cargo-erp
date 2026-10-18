package org.transporte.viajes.application.port.out;

import org.transporte.viajes.domian.bean.ConductorInfo;

public interface ConductorOutService {

    // lanza RecursoNoEncontradoException si no existe y ServicioNoDisponibleException si MS-Conductores no responde
    ConductorInfo buscarPorId(Long id);
}
