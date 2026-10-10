package org.transporte.carga.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.carga.application.port.in.MercanciaUseCase;
import org.transporte.carga.application.port.out.MercanciaOutService;
import org.transporte.carga.domian.bean.Mercancia;
import org.transporte.carga.domian.constraints.MercanciaConstraint;
import org.transporte.carga.domian.model.RecursoDuplicadoException;
import org.transporte.carga.domian.model.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MercanciaService implements MercanciaUseCase {

    private final MercanciaOutService mercanciaOutService;
    private final MercanciaConstraint mercanciaConstraint;

    @Override
    @Transactional
    public Mercancia crear(Mercancia mercancia) {
        mercanciaConstraint.validarClaseRiesgo(mercancia);
        if (mercanciaOutService.existePorDescripcion(mercancia.getDescripcion())) {
            throw new RecursoDuplicadoException("Ya existe una mercancia con la descripcion " + mercancia.getDescripcion());
        }
        return mercanciaOutService.guardar(mercancia);
    }

    @Override
    @Transactional
    public Mercancia actualizar(Long id, Mercancia mercancia) {
        Mercancia existente = buscarPorId(id);
        mercanciaConstraint.validarClaseRiesgo(mercancia);
        boolean cambioDescripcion = !existente.getDescripcion().equals(mercancia.getDescripcion());
        if (cambioDescripcion && mercanciaOutService.existePorDescripcion(mercancia.getDescripcion())) {
            throw new RecursoDuplicadoException("Ya existe una mercancia con la descripcion " + mercancia.getDescripcion());
        }

        existente.setDescripcion(mercancia.getDescripcion());
        existente.setTipo(mercancia.getTipo());
        existente.setClaseRiesgo(mercancia.getClaseRiesgo());
        existente.setPesoKg(mercancia.getPesoKg());
        return mercanciaOutService.guardar(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Mercancia buscarPorId(Long id) {
        return mercanciaOutService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la mercancia con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mercancia> listar() {
        return mercanciaOutService.listar();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        buscarPorId(id);
        mercanciaOutService.eliminar(id);
    }
}
