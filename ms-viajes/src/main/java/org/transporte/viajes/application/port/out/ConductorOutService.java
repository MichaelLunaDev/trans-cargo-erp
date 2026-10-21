package org.transporte.viajes.application.port.out;

import org.transporte.viajes.domian.bean.ConductorInfo;

public interface ConductorOutService {

    ConductorInfo buscarPorId(Long id);
}
