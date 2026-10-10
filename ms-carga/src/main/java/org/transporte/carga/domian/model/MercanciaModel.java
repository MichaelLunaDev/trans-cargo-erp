package org.transporte.carga.domian.model;

import org.springframework.stereotype.Component;
import org.transporte.carga.domian.bean.Mercancia;
import org.transporte.carga.domian.constraints.MercanciaConstraint;
import org.transporte.carga.domian.enums.TipoMercancia;

@Component
public class MercanciaModel implements MercanciaConstraint {

    @Override
    public void validarClaseRiesgo(Mercancia mercancia) {
        boolean sinClaseRiesgo = mercancia.getClaseRiesgo() == null || mercancia.getClaseRiesgo().isBlank();
        if (mercancia.getTipo() == TipoMercancia.PELIGROSA && sinClaseRiesgo) {
            throw new ReglaNegocioException("Una mercancia PELIGROSA debe indicar su clase de riesgo");
        }
    }
}
