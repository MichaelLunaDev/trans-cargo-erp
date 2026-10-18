package org.transporte.carga.application.port.in;

import java.util.List;

import org.transporte.carga.domian.bean.Pesaje;

public interface PesajeUseCase {

    Pesaje registrar(Pesaje pesaje);

    Pesaje buscarPorId(Long id);

    List<Pesaje> listarPorViaje(Long viajeId);

    List<Pesaje> listarTodos();

    void eliminar(Long id);
}
