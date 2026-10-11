package org.transporte.mantenimiento.domian.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;

class MantenimientoModelTest {

    private final OdometroModel odometroModel = new OdometroModel();
    private final NeumaticoModel neumaticoModel = new NeumaticoModel(0.9);
    private final RevisionModel revisionModel = new RevisionModel();
    private final PlacaModel placaModel = new PlacaModel();

    @Test
    void debeSumarKilometraje() {
        assertEquals(1350.0, odometroModel.sumarKilometraje(1000.0, 350.0));
        assertEquals(350.0, odometroModel.sumarKilometraje(null, 350.0));
    }

    @Test
    void debeRechazarDistanciaMenorOIgualACero() {
        assertThrows(ReglaNegocioException.class, () -> odometroModel.validarDistancia(0.0));
        assertThrows(ReglaNegocioException.class, () -> odometroModel.validarDistancia(-10.0));
        assertThrows(ReglaNegocioException.class, () -> odometroModel.validarDistancia(null));
    }

    @Test
    void debeDeterminarEstadoDelNeumatico() {
        assertEquals(EstadoNeumatico.BUENO, neumaticoModel.determinarEstado(89_999.0, 100_000.0));
        assertEquals(EstadoNeumatico.ALERTA, neumaticoModel.determinarEstado(90_000.0, 100_000.0));
        assertEquals(EstadoNeumatico.ALERTA, neumaticoModel.determinarEstado(99_999.0, 100_000.0));
        assertEquals(EstadoNeumatico.CAMBIO_REQUERIDO, neumaticoModel.determinarEstado(100_000.0, 100_000.0));
        assertEquals(EstadoNeumatico.CAMBIO_REQUERIDO, neumaticoModel.determinarEstado(120_000.0, 100_000.0));
    }

    @Test
    void debeVencerRevisionPreventivaPendienteSoloAlSuperarKmProgramado() {
        Revision revision = new Revision(1L, 5L, TipoRevision.PREVENTIVA, 20_000.0, null, EstadoRevision.PENDIENTE);

        assertFalse(revisionModel.estaVencida(revision, 20_000.0));
        assertTrue(revisionModel.estaVencida(revision, 20_000.5));

        revision.setEstado(EstadoRevision.REALIZADA);
        assertFalse(revisionModel.estaVencida(revision, 25_000.0));

        Revision correctiva = new Revision(2L, 5L, TipoRevision.CORRECTIVA, 20_000.0, null, EstadoRevision.PENDIENTE);
        assertFalse(revisionModel.estaVencida(correctiva, 25_000.0));
    }

    @Test
    void debeExigirKmEnRevisionPreventiva() {
        Revision sinKm = new Revision(null, 5L, TipoRevision.PREVENTIVA, null, null, null);
        assertThrows(ReglaNegocioException.class, () -> revisionModel.validarProgramacion(sinKm));

        Revision correctiva = new Revision(null, 5L, TipoRevision.CORRECTIVA, null, null, null);
        assertDoesNotThrow(() -> revisionModel.validarProgramacion(correctiva));
    }

    @Test
    void noDebeRealizarDosVecesLaMismaRevision() {
        Revision realizada = new Revision(1L, 5L, TipoRevision.PREVENTIVA, 20_000.0, null, EstadoRevision.REALIZADA);
        assertThrows(ReglaNegocioException.class, () -> revisionModel.validarRealizable(realizada));
    }

    @Test
    void debeValidarFormatoDePlaca() {
        assertDoesNotThrow(() -> placaModel.validarPlaca("ABC-123"));
        assertDoesNotThrow(() -> placaModel.validarPlaca("A1B234"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("AB-12"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("ABC_1234"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca(null));
    }
}
