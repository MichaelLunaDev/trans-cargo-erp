package org.transporte.mantenimiento.domian.constraints;

import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;

public interface NeumaticoConstraint {

    EstadoNeumatico determinarEstado(Double kmRecorridos, Double vidaUtilKm);
}
