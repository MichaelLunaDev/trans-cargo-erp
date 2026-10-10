package org.transporte.carga.application.port.in;

import java.util.List;

import org.transporte.carga.domian.bean.Mercancia;

public interface MercanciaUseCase {

    Mercancia crear(Mercancia mercancia);

    Mercancia actualizar(Long id, Mercancia mercancia);

    Mercancia buscarPorId(Long id);

    List<Mercancia> listar();

    void eliminar(Long id);
}
