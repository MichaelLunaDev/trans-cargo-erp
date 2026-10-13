package org.transporte.viajes.domian.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;

class ViajeModelTest {

    private final ViajeModel viajeModel = new ViajeModel();
    private final PlacaModel placaModel = new PlacaModel();

    private Viaje viaje(String origen, String destino, Double distanciaKm, EstadoViaje estado) {
        return new Viaje(1L, 7L, "ABC-123", 3L, origen, destino, distanciaKm,
                LocalDateTime.of(2026, 11, 1, 8, 0), null, estado);
    }

    @Test
    void debeValidarDistanciaOrigenYDestino() {
        assertDoesNotThrow(() -> viajeModel.validarDatos(viaje("Lima", "Arequipa", 1010.0, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje("Lima", "Arequipa", 0.0, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje("Lima", "Arequipa", -5.0, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje("Lima", "Arequipa", null, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje(" ", "Arequipa", 10.0, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje("Lima", null, 10.0, null)));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarDatos(viaje("Lima", "LIMA ", 10.0, null)));
    }

    @Test
    void debePermitirSoloLasTransicionesValidas() {
        assertDoesNotThrow(() -> viajeModel.validarTransicion(viaje("A", "B", 1.0, EstadoViaje.PROGRAMADO),
                EstadoViaje.EN_CURSO));
        assertDoesNotThrow(() -> viajeModel.validarTransicion(viaje("A", "B", 1.0, EstadoViaje.EN_CURSO),
                EstadoViaje.FINALIZADO));
        assertDoesNotThrow(() -> viajeModel.validarTransicion(viaje("A", "B", 1.0, EstadoViaje.PROGRAMADO),
                EstadoViaje.CANCELADO));

        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarTransicion(
                viaje("A", "B", 1.0, EstadoViaje.PROGRAMADO), EstadoViaje.FINALIZADO));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarTransicion(
                viaje("A", "B", 1.0, EstadoViaje.EN_CURSO), EstadoViaje.CANCELADO));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarTransicion(
                viaje("A", "B", 1.0, EstadoViaje.FINALIZADO), EstadoViaje.EN_CURSO));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarTransicion(
                viaje("A", "B", 1.0, EstadoViaje.CANCELADO), EstadoViaje.EN_CURSO));
        assertThrows(ReglaNegocioException.class, () -> viajeModel.validarTransicion(
                viaje("A", "B", 1.0, EstadoViaje.EN_CURSO), EstadoViaje.EN_CURSO));
    }

    @Test
    void elMismoViajeNoCuentaComoOtroViajeEnCurso() {
        Viaje actual = viaje("A", "B", 1.0, EstadoViaje.PROGRAMADO);
        Viaje mismo = viaje("A", "B", 1.0, EstadoViaje.EN_CURSO);
        Viaje otro = viaje("A", "B", 1.0, EstadoViaje.EN_CURSO);
        otro.setId(2L);

        assertDoesNotThrow(() -> viajeModel.validarSinViajeEnCurso(actual, List.of(), List.of()));
        assertDoesNotThrow(() -> viajeModel.validarSinViajeEnCurso(actual, List.of(mismo), List.of(mismo)));
        assertThrows(ReglaNegocioException.class,
                () -> viajeModel.validarSinViajeEnCurso(actual, List.of(otro), List.of()));
        assertThrows(ReglaNegocioException.class,
                () -> viajeModel.validarSinViajeEnCurso(actual, List.of(), List.of(otro)));
    }

    @Test
    void debeValidarFormatoDePlaca() {
        assertDoesNotThrow(() -> placaModel.validarPlaca("ABC-123"));
        assertDoesNotThrow(() -> placaModel.validarPlaca("ABC123"));
        assertDoesNotThrow(() -> placaModel.validarPlaca("F1A-9B2"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("AB-1234"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("ABC-12"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca("ABC--123"));
        assertThrows(ReglaNegocioException.class, () -> placaModel.validarPlaca(null));
    }
}
