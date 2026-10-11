package org.transporte.mantenimiento.domian.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.domian.constraints.NeumaticoConstraint;
import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;

@Component
public class NeumaticoModel implements NeumaticoConstraint {

    private final double umbralAlerta;

    public NeumaticoModel(@Value("${mantenimiento.umbral-alerta}") double umbralAlerta) {
        this.umbralAlerta = umbralAlerta;
    }

    @Override
    public EstadoNeumatico determinarEstado(Double kmRecorridos, Double vidaUtilKm) {
        if (kmRecorridos >= vidaUtilKm) {
            return EstadoNeumatico.CAMBIO_REQUERIDO;
        }
        if (kmRecorridos >= vidaUtilKm * umbralAlerta) {
            return EstadoNeumatico.ALERTA;
        }
        return EstadoNeumatico.BUENO;
    }
}
