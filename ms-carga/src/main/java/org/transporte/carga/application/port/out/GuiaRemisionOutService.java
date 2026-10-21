package org.transporte.carga.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.carga.domian.bean.GuiaRemision;

public interface GuiaRemisionOutService {

    GuiaRemision guardar(GuiaRemision guiaRemision);

    Optional<GuiaRemision> buscarPorId(Long id);

    List<GuiaRemision> listarPorViaje(Long viajeId);

    Integer obtenerUltimoNumero(String serie);

    void eliminar(Long id);
}
