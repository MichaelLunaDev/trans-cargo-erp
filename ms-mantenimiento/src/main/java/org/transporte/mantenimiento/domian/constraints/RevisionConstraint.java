package org.transporte.mantenimiento.domian.constraints;

import org.transporte.mantenimiento.domian.bean.Revision;

public interface RevisionConstraint {

    void validarProgramacion(Revision revision);

    void validarRealizable(Revision revision);

    boolean estaVencida(Revision revision, Double kilometrajeAcumulado);
}
