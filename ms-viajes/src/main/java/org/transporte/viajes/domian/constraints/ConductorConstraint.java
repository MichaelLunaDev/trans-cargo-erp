package org.transporte.viajes.domian.constraints;

import java.time.LocalDateTime;

import org.transporte.viajes.domian.bean.ConductorInfo;

public interface ConductorConstraint {

    void validarApto(ConductorInfo conductor, LocalDateTime fechaSalida);
}
