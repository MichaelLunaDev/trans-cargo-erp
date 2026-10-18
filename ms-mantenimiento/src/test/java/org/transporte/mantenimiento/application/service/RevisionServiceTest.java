package org.transporte.mantenimiento.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.application.port.out.RevisionOutService;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;
import org.transporte.mantenimiento.domian.model.RecursoNoEncontradoException;
import org.transporte.mantenimiento.domian.model.RevisionModel;

class RevisionServiceTest {

    private RevisionOutService revisionOutService;
    private RevisionService service;

    @BeforeEach
    void setUp() {
        revisionOutService = mock(RevisionOutService.class);
        service = new RevisionService(revisionOutService, mock(OdometroOutService.class), new RevisionModel());
    }

    @Test
    void debeEliminarRevisionExistente() {
        Revision revision = new Revision(10L, 7L, TipoRevision.PREVENTIVA, 10000.0, LocalDate.of(2026, 12, 1),
                EstadoRevision.PENDIENTE);
        when(revisionOutService.buscarPorId(10L)).thenReturn(Optional.of(revision));

        service.eliminar(10L);

        verify(revisionOutService).eliminar(10L);
    }

    @Test
    void eliminarRevisionInexistenteDebeLanzarNoEncontrado() {
        when(revisionOutService.buscarPorId(50L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(50L));
        verify(revisionOutService, never()).eliminar(anyLong());
    }
}
