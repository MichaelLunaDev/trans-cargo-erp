package org.transporte.carga.domian.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.transporte.carga.domian.enums.EstadoPesaje;

class PesajeModelTest {

    private final PesajeModel pesajeModel = new PesajeModel(48000, 11000);
    private final PlacaModel placaModel = new PlacaModel();

    @Test
    void debeCalcularPesoNeto() {
        assertEquals(22000.0, pesajeModel.calcularPesoNeto(8000.0, 30000.0));
    }

    @Test
    void debeRechazarPesoBrutoMenorOIgualALaTara() {
        assertThrows(ReglaNegocioException.class, () -> pesajeModel.validarPesos(8000.0, 8000.0));
        assertThrows(ReglaNegocioException.class, () -> pesajeModel.validarPesos(8000.0, 5000.0));
    }

    @Test
    void debeAprobarPesajeDentroDeLimites() {
        assertEquals(EstadoPesaje.APROBADO, pesajeModel.determinarEstado(30000.0, 3));
    }

    @Test
    void debeObservarPesajeQueSuperaPesoBrutoMaximo() {
        assertEquals(EstadoPesaje.OBSERVADO, pesajeModel.determinarEstado(50000.0, 6));
    }

    @Test
    void debeObservarPesajeQueSuperaPesoPorEje() {
        assertEquals(EstadoPesaje.OBSERVADO, pesajeModel.determinarEstado(36000.0, 3));
    }

    @Test
    void debeValidarFormatoDePlaca() {
        assertDoesNotThrow(() -> placaModel.validarPlaca("ABC-123"));
        assertDoesNotThrow(() -> placaModel.validarPlaca("A1B234"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("AB-12"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("ABC_1234"));
    }
}
