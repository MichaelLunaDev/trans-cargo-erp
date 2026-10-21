package org.transporte.viajes.application.port.out;

import org.transporte.viajes.domian.bean.VehiculoInfo;

public interface VehiculoOutService {

    VehiculoInfo buscarPorId(Long id);
}
