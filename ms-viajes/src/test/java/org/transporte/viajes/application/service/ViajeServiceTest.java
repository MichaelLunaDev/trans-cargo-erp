package org.transporte.viajes.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.transporte.viajes.application.port.out.ConductorOutService;
import org.transporte.viajes.application.port.out.VehiculoOutService;
import org.transporte.viajes.application.port.out.ViajeEventoOutService;
import org.transporte.viajes.application.port.out.ViajeOutService;
import org.transporte.viajes.domian.bean.ConductorInfo;
import org.transporte.viajes.domian.bean.VehiculoInfo;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.bean.ViajeIniciado;
import org.transporte.viajes.domian.enums.EstadoConductor;
import org.transporte.viajes.domian.enums.EstadoVehiculo;
import org.transporte.viajes.domian.enums.EstadoViaje;
import org.transporte.viajes.domian.model.ConductorModel;
import org.transporte.viajes.domian.model.PlacaModel;
import org.transporte.viajes.domian.model.RecursoDuplicadoException;
import org.transporte.viajes.domian.model.RecursoNoEncontradoException;
import org.transporte.viajes.domian.model.ReglaNegocioException;
import org.transporte.viajes.domian.model.ServicioNoDisponibleException;
import org.transporte.viajes.domian.model.VehiculoModel;
import org.transporte.viajes.domian.model.ViajeModel;

class ViajeServiceTest {

    private static final LocalDateTime SALIDA = LocalDateTime.of(2026, 11, 1, 8, 0);
    private static final LocalDate VIGENTE = LocalDate.of(2027, 1, 1);
    private static final LocalDate VENCIDO = LocalDate.of(2026, 10, 31);

    private ViajeOutService viajeOutService;
    private VehiculoOutService vehiculoOutService;
    private ConductorOutService conductorOutService;
    private ViajeEventoOutService viajeEventoOutService;
    private ViajeService service;

    @BeforeEach
    void setUp() {
        viajeOutService = mock(ViajeOutService.class);
        vehiculoOutService = mock(VehiculoOutService.class);
        conductorOutService = mock(ConductorOutService.class);
        viajeEventoOutService = mock(ViajeEventoOutService.class);
        service = new ViajeService(viajeOutService, vehiculoOutService, conductorOutService, viajeEventoOutService,
                new ViajeModel(), new VehiculoModel(), new ConductorModel(), new PlacaModel());

        when(viajeOutService.guardar(any(Viaje.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private Viaje nuevoViaje() {
        return new Viaje(99L, 7L, null, 3L, "Lima", "Arequipa", 1010.5, SALIDA, null, null);
    }

    private Viaje viajeGuardado(Long id, EstadoViaje estado) {
        return new Viaje(id, 7L, "ABC-123", 3L, "Lima", "Arequipa", 1010.5, SALIDA, null, estado);
    }

    private VehiculoInfo vehiculo(EstadoVehiculo estado, LocalDate soat, LocalDate citv) {
        return new VehiculoInfo(7L, "abc-123", estado, soat, citv);
    }

    private ConductorInfo conductor(EstadoConductor estado, LocalDate brevete) {
        return new ConductorInfo(3L, estado, brevete);
    }

    private void prepararRemotos(VehiculoInfo vehiculo, ConductorInfo conductor) {
        when(vehiculoOutService.buscarPorId(7L)).thenReturn(vehiculo);
        when(conductorOutService.buscarPorId(3L)).thenReturn(conductor);
    }

    // ---------- programar ----------

    @Test
    void debeProgramarViajeCorrecto() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));

        Viaje viaje = service.programar(nuevoViaje());

        assertNull(viaje.getId());
        assertEquals(EstadoViaje.PROGRAMADO, viaje.getEstado());
        assertEquals("ABC-123", viaje.getPlaca());
        assertEquals(7L, viaje.getVehiculoId());
        assertEquals(3L, viaje.getConductorId());
        assertNull(viaje.getFechaLlegada());
        verify(viajeOutService).guardar(viaje);
    }

    @Test
    void debeAceptarDocumentosQueVencenElMismoDiaDeSalida() {
        LocalDate diaSalida = SALIDA.toLocalDate();
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, diaSalida, diaSalida),
                conductor(EstadoConductor.HABILITADO, diaSalida));

        assertDoesNotThrow(() -> service.programar(nuevoViaje()));
    }

    @Test
    void debeRechazarSoatVencido() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VENCIDO, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));

        assertRechazado();
    }

    @Test
    void debeRechazarCitvVencido() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VENCIDO),
                conductor(EstadoConductor.HABILITADO, VIGENTE));

        assertRechazado();
    }

    @Test
    void debeRechazarSoatSinFecha() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, null, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));

        assertRechazado();
    }

    @Test
    void debeRechazarBreveteVencido() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VENCIDO));

        assertRechazado();
    }

    @Test
    void debeRechazarVehiculoInactivo() {
        prepararRemotos(vehiculo(EstadoVehiculo.EN_MANTENIMIENTO, VIGENTE, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));
        assertRechazado();

        prepararRemotos(vehiculo(EstadoVehiculo.DE_BAJA, VIGENTE, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));
        assertRechazado();
    }

    @Test
    void debeRechazarConductorInactivo() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VIGENTE),
                conductor(EstadoConductor.SUSPENDIDO, VIGENTE));

        assertRechazado();
    }

    @Test
    void debeRechazarOrigenIgualADestinoSinConsultarOtrosServicios() {
        Viaje viaje = nuevoViaje();
        viaje.setDestino(" lima ");

        assertThrows(ReglaNegocioException.class, () -> service.programar(viaje));
        verify(vehiculoOutService, never()).buscarPorId(anyLong());
        verify(viajeOutService, never()).guardar(any());
    }

    @Test
    void debePropagarVehiculoNoEncontradoYServicioNoDisponible() {
        when(vehiculoOutService.buscarPorId(7L)).thenThrow(new RecursoNoEncontradoException("no existe"));
        assertThrows(RecursoNoEncontradoException.class, () -> service.programar(nuevoViaje()));

        doReturn(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VIGENTE)).when(vehiculoOutService).buscarPorId(7L);
        when(conductorOutService.buscarPorId(3L)).thenThrow(new ServicioNoDisponibleException("caido"));
        assertThrows(ServicioNoDisponibleException.class, () -> service.programar(nuevoViaje()));

        verify(viajeOutService, never()).guardar(any());
    }

    @Test
    void debeRechazarViajeDuplicadoMismaFechaDeSalida() {
        prepararRemotos(vehiculo(EstadoVehiculo.DISPONIBLE, VIGENTE, VIGENTE),
                conductor(EstadoConductor.HABILITADO, VIGENTE));
        when(viajeOutService.existePorVehiculoYFechaSalida(any(), any(), anyList())).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> service.programar(nuevoViaje()));
        verify(viajeOutService, never()).guardar(any());
    }

    private void assertRechazado() {
        assertThrows(ReglaNegocioException.class, () -> service.programar(nuevoViaje()));
        verify(viajeOutService, never()).guardar(any());
    }

    // ---------- iniciar ----------

    @Test
    void iniciarDebePublicarElEventoUnaSolaVezConLosDatosCorrectos() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        LocalDateTime antes = LocalDateTime.now().withNano(0);

        Viaje viaje = service.iniciar(10L);

        assertEquals(EstadoViaje.EN_CURSO, viaje.getEstado());
        ArgumentCaptor<ViajeIniciado> evento = ArgumentCaptor.forClass(ViajeIniciado.class);
        verify(viajeEventoOutService, times(1)).publicarViajeIniciado(evento.capture());
        ViajeIniciado enviado = evento.getValue();
        assertNotNull(UUID.fromString(enviado.getEventId()));
        assertEquals(10L, enviado.getViajeId());
        assertEquals(7L, enviado.getVehiculoId());
        assertEquals("ABC-123", enviado.getPlaca());
        assertEquals(1010.5, enviado.getDistanciaKm());
        // la fecha del evento es la hora de inicio, sin fracciones de segundo (yyyy-MM-ddTHH:mm:ss)
        assertNotNull(enviado.getFecha());
        assertEquals(0, enviado.getFecha().getNano());
        assertFalse(enviado.getFecha().isBefore(antes));
    }

    @Test
    void cadaInicioDebeGenerarUnEventIdDistinto() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.buscarPorId(11L)).thenReturn(Optional.of(viajeGuardado(11L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.listarPorVehiculoYEstado(7L, EstadoViaje.EN_CURSO)).thenReturn(List.of());

        service.iniciar(10L);
        service.iniciar(11L);

        ArgumentCaptor<ViajeIniciado> evento = ArgumentCaptor.forClass(ViajeIniciado.class);
        verify(viajeEventoOutService, times(2)).publicarViajeIniciado(evento.capture());
        assertNotEquals(evento.getAllValues().get(0).getEventId(), evento.getAllValues().get(1).getEventId());
    }

    @Test
    void iniciarDebeGuardarAntesDePublicar() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));

        service.iniciar(10L);

        InOrder orden = inOrder(viajeOutService, viajeEventoOutService);
        orden.verify(viajeOutService).guardar(any(Viaje.class));
        orden.verify(viajeEventoOutService).publicarViajeIniciado(any(ViajeIniciado.class));
    }

    @Test
    void debeRechazarIniciarViajeFinalizadoOCancelado() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.FINALIZADO)));
        when(viajeOutService.buscarPorId(11L)).thenReturn(Optional.of(viajeGuardado(11L, EstadoViaje.CANCELADO)));
        when(viajeOutService.buscarPorId(12L)).thenReturn(Optional.of(viajeGuardado(12L, EstadoViaje.EN_CURSO)));

        assertThrows(ReglaNegocioException.class, () -> service.iniciar(10L));
        assertThrows(ReglaNegocioException.class, () -> service.iniciar(11L));
        assertThrows(ReglaNegocioException.class, () -> service.iniciar(12L));
        verify(viajeOutService, never()).guardar(any());
        verify(viajeEventoOutService, never()).publicarViajeIniciado(any());
    }

    @Test
    void debeRechazarIniciarSiElVehiculoTieneOtroViajeEnCurso() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.listarPorVehiculoYEstado(7L, EstadoViaje.EN_CURSO))
                .thenReturn(List.of(viajeGuardado(5L, EstadoViaje.EN_CURSO)));

        assertThrows(ReglaNegocioException.class, () -> service.iniciar(10L));
        verify(viajeOutService, never()).guardar(any());
        verify(viajeEventoOutService, never()).publicarViajeIniciado(any());
    }

    @Test
    void debeRechazarIniciarSiElConductorTieneOtroViajeEnCurso() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.listarPorConductorYEstado(3L, EstadoViaje.EN_CURSO))
                .thenReturn(List.of(viajeGuardado(6L, EstadoViaje.EN_CURSO)));

        assertThrows(ReglaNegocioException.class, () -> service.iniciar(10L));
        verify(viajeOutService, never()).guardar(any());
        verify(viajeEventoOutService, never()).publicarViajeIniciado(any());
    }

    @Test
    void siFallaLaPublicacionElViajeIgualQuedaEnCurso() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        doThrow(new RuntimeException("RabbitMQ caido")).when(viajeEventoOutService).publicarViajeIniciado(any());

        Viaje viaje = assertDoesNotThrow(() -> service.iniciar(10L));

        assertEquals(EstadoViaje.EN_CURSO, viaje.getEstado());
        ArgumentCaptor<Viaje> guardado = ArgumentCaptor.forClass(Viaje.class);
        verify(viajeOutService).guardar(guardado.capture());
        assertEquals(EstadoViaje.EN_CURSO, guardado.getValue().getEstado());
    }

    @Test
    void iniciarViajeInexistenteDebeLanzarNoEncontrado() {
        when(viajeOutService.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.iniciar(50L));
    }

    // ---------- finalizar y cancelar ----------

    @Test
    void debeFinalizarViajeEnCursoSinPublicarEvento() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.EN_CURSO)));

        Viaje viaje = service.finalizar(10L);

        assertEquals(EstadoViaje.FINALIZADO, viaje.getEstado());
        assertNotNull(viaje.getFechaLlegada());
        verify(viajeEventoOutService, never()).publicarViajeIniciado(any());
    }

    @Test
    void debeRechazarFinalizarViajeProgramado() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));

        assertThrows(ReglaNegocioException.class, () -> service.finalizar(10L));
    }

    @Test
    void debeCancelarSoloViajesProgramados() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.buscarPorId(11L)).thenReturn(Optional.of(viajeGuardado(11L, EstadoViaje.EN_CURSO)));

        assertEquals(EstadoViaje.CANCELADO, service.cancelar(10L).getEstado());
        assertThrows(ReglaNegocioException.class, () -> service.cancelar(11L));
    }

    // ---------- eliminar ----------

    @Test
    void debeEliminarViajesProgramadosOCancelados() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.PROGRAMADO)));
        when(viajeOutService.buscarPorId(11L)).thenReturn(Optional.of(viajeGuardado(11L, EstadoViaje.CANCELADO)));

        service.eliminar(10L);
        service.eliminar(11L);

        verify(viajeOutService).eliminar(10L);
        verify(viajeOutService).eliminar(11L);
    }

    @Test
    void debeRechazarEliminarViajeEnCursoOFinalizado() {
        when(viajeOutService.buscarPorId(10L)).thenReturn(Optional.of(viajeGuardado(10L, EstadoViaje.EN_CURSO)));
        when(viajeOutService.buscarPorId(11L)).thenReturn(Optional.of(viajeGuardado(11L, EstadoViaje.FINALIZADO)));

        assertThrows(ReglaNegocioException.class, () -> service.eliminar(10L));
        assertThrows(ReglaNegocioException.class, () -> service.eliminar(11L));
        verify(viajeOutService, never()).eliminar(anyLong());
    }

    @Test
    void eliminarViajeInexistenteDebeLanzarNoEncontrado() {
        when(viajeOutService.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(50L));
        verify(viajeOutService, never()).eliminar(anyLong());
    }
}
