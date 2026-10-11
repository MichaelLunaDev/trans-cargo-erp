package org.transporte.mantenimiento.domian.model;

import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.constraints.RevisionConstraint;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;

@Component
public class RevisionModel implements RevisionConstraint {

    @Override
    public void validarProgramacion(Revision revision) {
        if (revision.getTipo() == TipoRevision.PREVENTIVA && revision.getKmProgramado() == null) {
            throw new ReglaNegocioException("Una revision preventiva debe indicar el kilometraje programado");
        }
    }

    @Override
    public void validarRealizable(Revision revision) {
        if (revision.getEstado() == EstadoRevision.REALIZADA) {
            throw new ReglaNegocioException("La revision con id " + revision.getId() + " ya fue realizada");
        }
    }

    @Override
    public boolean estaVencida(Revision revision, Double kilometrajeAcumulado) {
        return revision.getTipo() == TipoRevision.PREVENTIVA
                && revision.getEstado() == EstadoRevision.PENDIENTE
                && revision.getKmProgramado() != null
                && kilometrajeAcumulado != null
                && kilometrajeAcumulado > revision.getKmProgramado();
    }
}
