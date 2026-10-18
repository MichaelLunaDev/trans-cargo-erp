package org.transporte.carga.application.port.in;

import java.util.List;

import org.transporte.carga.domian.bean.GuiaRemision;

public interface GuiaRemisionUseCase {

    GuiaRemision emitir(GuiaRemision guiaRemision);

    GuiaRemision buscarPorId(Long id);

    List<GuiaRemision> listarPorViaje(Long viajeId);

    GuiaRemision anular(Long id);

    void eliminar(Long id);
}
