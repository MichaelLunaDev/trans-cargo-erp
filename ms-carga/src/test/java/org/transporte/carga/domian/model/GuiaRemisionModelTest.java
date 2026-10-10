package org.transporte.carga.domian.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.transporte.carga.domian.bean.GuiaRemision;
import org.transporte.carga.domian.bean.Mercancia;
import org.transporte.carga.domian.enums.EstadoGuia;
import org.transporte.carga.domian.enums.TipoMercancia;

class GuiaRemisionModelTest {

    private final GuiaRemisionModel guiaRemisionModel = new GuiaRemisionModel();
    private final MercanciaModel mercanciaModel = new MercanciaModel();

    @Test
    void debeValidarFormatoDeSerie() {
        assertDoesNotThrow(() -> guiaRemisionModel.validarSerie("V001"));
        assertDoesNotThrow(() -> guiaRemisionModel.validarSerie("T0A1"));
        assertThrows(ReglaNegocioException.class, () -> guiaRemisionModel.validarSerie("0001"));
        assertThrows(ReglaNegocioException.class, () -> guiaRemisionModel.validarSerie("V01"));
    }

    @Test
    void noDebeAnularUnaGuiaYaAnulada() {
        GuiaRemision guia = new GuiaRemision();
        guia.setSerie("V001");
        guia.setNumero(1);

        guia.setEstado(EstadoGuia.EMITIDA);
        assertDoesNotThrow(() -> guiaRemisionModel.validarAnulacion(guia));

        guia.setEstado(EstadoGuia.ANULADA);
        assertThrows(ReglaNegocioException.class, () -> guiaRemisionModel.validarAnulacion(guia));
    }

    @Test
    void mercanciaPeligrosaDebeTenerClaseRiesgo() {
        Mercancia peligrosa = new Mercancia(null, "Combustible diesel", TipoMercancia.PELIGROSA, null, 20000.0);
        assertThrows(ReglaNegocioException.class, () -> mercanciaModel.validarClaseRiesgo(peligrosa));

        peligrosa.setClaseRiesgo("3");
        assertDoesNotThrow(() -> mercanciaModel.validarClaseRiesgo(peligrosa));

        Mercancia general = new Mercancia(null, "Cajas de fruta", TipoMercancia.GENERAL, null, 5000.0);
        assertDoesNotThrow(() -> mercanciaModel.validarClaseRiesgo(general));
    }
}
