package org.transporte.carga.domian.constraints;

import org.transporte.carga.domian.enums.EstadoPesaje;

public interface PesajeConstraint {

    void validarPesos(Double taraKg, Double pesoBrutoKg);

    Double calcularPesoNeto(Double taraKg, Double pesoBrutoKg);

    EstadoPesaje determinarEstado(Double pesoBrutoKg, Integer cantidadEjes);
}
