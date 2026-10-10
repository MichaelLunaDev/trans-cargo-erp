package org.transporte.carga.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.carga.application.port.out.MercanciaOutService;
import org.transporte.carga.domian.bean.Mercancia;
import org.transporte.carga.infrastructure.persistence.entity.MercanciaEntity;
import org.transporte.carga.infrastructure.persistence.repository.MercanciaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MercanciaAdapter implements MercanciaOutService {

    private final MercanciaRepository mercanciaRepository;
    private final ModelMapper modelMapper;

    @Override
    public Mercancia guardar(Mercancia mercancia) {
        MercanciaEntity entity = modelMapper.map(mercancia, MercanciaEntity.class);
        return modelMapper.map(mercanciaRepository.save(entity), Mercancia.class);
    }

    @Override
    public Optional<Mercancia> buscarPorId(Long id) {
        return mercanciaRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Mercancia.class));
    }

    @Override
    public List<Mercancia> listar() {
        return mercanciaRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Mercancia.class))
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        mercanciaRepository.deleteById(id);
    }

    @Override
    public boolean existePorDescripcion(String descripcion) {
        return mercanciaRepository.existsByDescripcion(descripcion);
    }
}
