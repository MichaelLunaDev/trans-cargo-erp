package org.transporte.carga.domian.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.transporte.carga.domian.constraints.PesajeConstraint;
import org.transporte.carga.domian.enums.EstadoPesaje;

@Component
public class PesajeModel implements PesajeConstraint {

    private final double pesoBrutoMaximoKg;
    private final double pesoMaximoPorEjeKg;

    public PesajeModel(@Value("${carga.peso-bruto-maximo-kg}") double pesoBrutoMaximoKg,
                       @Value("${carga.peso-maximo-por-eje-kg}") double pesoMaximoPorEjeKg) {
        this.pesoBrutoMaximoKg = pesoBrutoMaximoKg;
        this.pesoMaximoPorEjeKg = pesoMaximoPorEjeKg;
    }

    @Override
    public void validarPesos(Double taraKg, Double pesoBrutoKg) {
        if (pesoBrutoKg <= taraKg) {
            throw new ReglaNegocioException("El peso bruto debe ser mayor que la tara");
        }
    }

    @Override
    public Double calcularPesoNeto(Double taraKg, Double pesoBrutoKg) {
        return pesoBrutoKg - taraKg;
    }

    @Override
    public EstadoPesaje determinarEstado(Double pesoBrutoKg, Integer cantidadEjes) {
        double pesoPorEje = pesoBrutoKg / cantidadEjes;
        if (pesoBrutoKg > pesoBrutoMaximoKg || pesoPorEje > pesoMaximoPorEjeKg) {
            return EstadoPesaje.OBSERVADO;
        }
        return EstadoPesaje.APROBADO;
    }
}
