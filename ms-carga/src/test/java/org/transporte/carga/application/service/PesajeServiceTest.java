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
import org.transporte.carga.application.port.out.PesajeOutService;
import org.transporte.carga.domian.bean.Pesaje;
import org.transporte.carga.domian.enums.EstadoPesaje;
import org.transporte.carga.domian.model.PesajeModel;
import org.transporte.carga.domian.model.PlacaModel;
import org.transporte.carga.domian.model.RecursoNoEncontradoException;

class PesajeServiceTest {

    private PesajeOutService pesajeOutService;
    private PesajeService service;

    @BeforeEach
    void setUp() {
        pesajeOutService = mock(PesajeOutService.class);
        service = new PesajeService(pesajeOutService, new PesajeModel(48000, 11000), new PlacaModel());
    }

    @Test
    void debeEliminarPesajeExistente() {
        Pesaje pesaje = new Pesaje();
        pesaje.setId(10L);
        pesaje.setEstado(EstadoPesaje.APROBADO);
        when(pesajeOutService.buscarPorId(10L)).thenReturn(Optional.of(pesaje));

        service.eliminar(10L);

        verify(pesajeOutService).eliminar(10L);
    }

    @Test
    void eliminarPesajeInexistenteDebeLanzarNoEncontrado() {
        when(pesajeOutService.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(50L));
        verify(pesajeOutService, never()).eliminar(anyLong());
    }
}
