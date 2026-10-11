package org.transporte.mantenimiento.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.transporte.mantenimiento.application.port.out.EventoProcesadoOutService;
import org.transporte.mantenimiento.application.port.out.NeumaticoOutService;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.application.port.out.RevisionOutService;
import org.transporte.mantenimiento.domian.bean.Neumatico;
import org.transporte.mantenimiento.domian.bean.Odometro;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.bean.ViajeIniciado;
import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;
import org.transporte.mantenimiento.domian.model.NeumaticoModel;
import org.transporte.mantenimiento.domian.model.OdometroModel;
import org.transporte.mantenimiento.domian.model.PlacaModel;
import org.transporte.mantenimiento.domian.model.ReglaNegocioException;
import org.transporte.mantenimiento.domian.model.RevisionModel;

class RegistrarViajeServiceTest {

    private OdometroOutService odometroOutService;
    private NeumaticoOutService neumaticoOutService;
    private RevisionOutService revisionOutService;
    private EventoProcesadoOutService eventoProcesadoOutService;
    private RegistrarViajeService service;

    @BeforeEach
    void setUp() {
        odometroOutService = mock(OdometroOutService.class);
        neumaticoOutService = mock(NeumaticoOutService.class);
        revisionOutService = mock(RevisionOutService.class);
        eventoProcesadoOutService = mock(EventoProcesadoOutService.class);
        service = new RegistrarViajeService(odometroOutService, neumaticoOutService, revisionOutService,
                eventoProcesadoOutService, new OdometroModel(), new NeumaticoModel(0.9), new RevisionModel(),
                new PlacaModel());

        when(odometroOutService.guardar(any(Odometro.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private ViajeIniciado viaje(String eventId, double distanciaKm) {
        return new ViajeIniciado(eventId, 10L, 7L, "abc-123", distanciaKm, LocalDateTime.of(2026, 10, 10, 8, 30));
    }

    @Test
    void debeSumarKmAlOdometroNeumaticosYVencerRevisiones() {
        when(eventoProcesadoOutService.existe("e-1")).thenReturn(false);
        when(odometroOutService.buscarPorVehiculoId(7L))
                .thenReturn(Optional.of(new Odometro(1L, 7L, "ABC-123", 19_800.0, null)));
        Neumatico neumatico = new Neumatico(3L, 7L, "DEL-IZQ", 100_000.0, 89_800.0, EstadoNeumatico.BUENO);
        when(neumaticoOutService.listarPorVehiculo(7L)).thenReturn(List.of(neumatico));
        Revision revision = new Revision(4L, 7L, TipoRevision.PREVENTIVA, 20_000.0, null, EstadoRevision.PENDIENTE);
        when(revisionOutService.listarPorVehiculoYEstado(7L, EstadoRevision.PENDIENTE)).thenReturn(List.of(revision));

        service.registrar(viaje("e-1", 350.0));

        ArgumentCaptor<Odometro> odometro = ArgumentCaptor.forClass(Odometro.class);
        verify(odometroOutService).guardar(odometro.capture());
        assertEquals(20_150.0, odometro.getValue().getKilometrajeAcumulado());
        assertEquals("ABC-123", odometro.getValue().getPlaca());

        assertEquals(90_150.0, neumatico.getKmRecorridos());
        assertEquals(EstadoNeumatico.ALERTA, neumatico.getEstado());
        verify(neumaticoOutService).guardar(neumatico);

        assertEquals(EstadoRevision.VENCIDA, revision.getEstado());
        verify(revisionOutService).guardar(revision);

        verify(eventoProcesadoOutService).registrar("e-1");
    }

    @Test
    void debeCrearOdometroSiElVehiculoNoTeniaViajes() {
        when(odometroOutService.buscarPorVehiculoId(7L)).thenReturn(Optional.empty());

        service.registrar(viaje("e-2", 120.0));

        ArgumentCaptor<Odometro> odometro = ArgumentCaptor.forClass(Odometro.class);
        verify(odometroOutService).guardar(odometro.capture());
        assertEquals(120.0, odometro.getValue().getKilometrajeAcumulado());
        assertEquals(7L, odometro.getValue().getVehiculoId());
    }

    @Test
    void debeIgnorarEventoRepetido() {
        when(eventoProcesadoOutService.existe("e-1")).thenReturn(true);

        service.registrar(viaje("e-1", 350.0));

        verify(odometroOutService, never()).guardar(any());
        verify(neumaticoOutService, never()).guardar(any());
        verify(eventoProcesadoOutService, never()).registrar(any());
    }

    @Test
    void debeRechazarDistanciaInvalida() {
        assertThrows(ReglaNegocioException.class, () -> service.registrar(viaje("e-3", 0.0)));
        verify(odometroOutService, never()).guardar(any());
        verify(eventoProcesadoOutService, never()).registrar(any());
    }
}
