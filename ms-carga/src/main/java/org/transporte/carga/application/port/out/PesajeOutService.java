package org.transporte.carga.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.carga.domian.bean.Pesaje;

public interface PesajeOutService {

    Pesaje guardar(Pesaje pesaje);

    Optional<Pesaje> buscarPorId(Long id);

    List<Pesaje> listarPorViaje(Long viajeId);

    List<Pesaje> listarTodos();

    void eliminar(Long id);
}
