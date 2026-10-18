package org.transporte.carga.domian.constraints;

import org.transporte.carga.domian.bean.GuiaRemision;

public interface GuiaRemisionConstraint {

    void validarSerie(String serie);

    void validarAnulacion(GuiaRemision guiaRemision);

    void validarEliminacion(GuiaRemision guiaRemision);
}
