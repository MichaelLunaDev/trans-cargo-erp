package org.transporte.carga.application.port.out;

import java.util.List;
import java.util.Optional;

import org.transporte.carga.domian.bean.Mercancia;

public interface MercanciaOutService {

    Mercancia guardar(Mercancia mercancia);

    Optional<Mercancia> buscarPorId(Long id);

    List<Mercancia> listar();

    void eliminar(Long id);

    boolean existePorDescripcion(String descripcion);
}
