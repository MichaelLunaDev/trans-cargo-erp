package org.transporte.viajes.application.port.out;

import org.transporte.viajes.domian.bean.VehiculoInfo;

public interface VehiculoOutService {

    // lanza RecursoNoEncontradoException si no existe y ServicioNoDisponibleException si MS-Flota no responde
    VehiculoInfo buscarPorId(Long id);
}
