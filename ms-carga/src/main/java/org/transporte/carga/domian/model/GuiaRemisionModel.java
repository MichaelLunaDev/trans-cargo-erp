package org.transporte.carga.domian.model;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.transporte.carga.domian.bean.GuiaRemision;
import org.transporte.carga.domian.constraints.GuiaRemisionConstraint;
import org.transporte.carga.domian.enums.EstadoGuia;

@Component
public class GuiaRemisionModel implements GuiaRemisionConstraint {

    private static final Pattern SERIE_PATTERN = Pattern.compile("^[A-Z][A-Z0-9]{3}$");

    @Override
    public void validarSerie(String serie) {
        if (serie == null || !SERIE_PATTERN.matcher(serie).matches()) {
            throw new ReglaNegocioException("La serie " + serie + " no tiene un formato valido (ejemplo: V001)");
        }
    }

    @Override
    public void validarAnulacion(GuiaRemision guiaRemision) {
        if (guiaRemision.getEstado() == EstadoGuia.ANULADA) {
            throw new ReglaNegocioException("La guia " + guiaRemision.getSerie() + "-" + guiaRemision.getNumero()
                    + " ya se encuentra anulada");
        }
    }
}
