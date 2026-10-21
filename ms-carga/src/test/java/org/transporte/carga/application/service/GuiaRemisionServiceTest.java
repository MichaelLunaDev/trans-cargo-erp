package org.transporte.carga.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.transporte.carga.application.port.out.GuiaRemisionOutService;
import org.transporte.carga.application.port.out.MercanciaOutService;
import org.transporte.carga.domian.bean.GuiaRemision;
import org.transporte.carga.domian.enums.EstadoGuia;
import org.transporte.carga.domian.model.GuiaRemisionModel;
import org.transporte.carga.domian.model.RecursoNoEncontradoException;
import org.transporte.carga.domian.model.ReglaNegocioException;

class GuiaRemisionServiceTest {

    private GuiaRemisionOutService guiaRemisionOutService;
    private GuiaRemisionService service;

    @BeforeEach
    void setUp() {
        guiaRemisionOutService = mock(GuiaRemisionOutService.class);
        service = new GuiaRemisionService(guiaRemisionOutService, mock(MercanciaOutService.class),
                new GuiaRemisionModel());
    }

    private GuiaRemision guia(Long id, EstadoGuia estado) {
        GuiaRemision guia = new GuiaRemision();
        guia.setId(id);
        guia.setSerie("V001");
        guia.setNumero(1);
        guia.setEstado(estado);
        return guia;
    }

    @Test
    void debeEliminarGuiaAnulada() {
        when(guiaRemisionOutService.buscarPorId(10L)).thenReturn(Optional.of(guia(10L, EstadoGuia.ANULADA)));

        service.eliminar(10L);

        verify(guiaRemisionOutService).eliminar(10L);
    }

    @Test
    void debeRechazarEliminarGuiaEmitida() {
        when(guiaRemisionOutService.buscarPorId(10L)).thenReturn(Optional.of(guia(10L, EstadoGuia.EMITIDA)));

        assertThrows(ReglaNegocioException.class, () -> service.eliminar(10L));
        verify(guiaRemisionOutService, never()).eliminar(anyLong());
    }

    @Test
    void eliminarGuiaInexistenteDebeLanzarNoEncontrado() {
        when(guiaRemisionOutService.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(50L));
        verify(guiaRemisionOutService, never()).eliminar(anyLong());
    }
}
