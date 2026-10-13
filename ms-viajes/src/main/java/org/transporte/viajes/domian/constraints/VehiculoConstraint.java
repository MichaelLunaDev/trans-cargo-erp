package org.transporte.viajes.domian.constraints;

import java.time.LocalDateTime;

import org.transporte.viajes.domian.bean.VehiculoInfo;

public interface VehiculoConstraint {

    void validarApto(VehiculoInfo vehiculo, LocalDateTime fechaSalida);
}
