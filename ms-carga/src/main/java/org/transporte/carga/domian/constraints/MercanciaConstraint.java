package org.transporte.carga.domian.constraints;

import org.transporte.carga.domian.bean.Mercancia;

public interface MercanciaConstraint {

    void validarClaseRiesgo(Mercancia mercancia);
}
