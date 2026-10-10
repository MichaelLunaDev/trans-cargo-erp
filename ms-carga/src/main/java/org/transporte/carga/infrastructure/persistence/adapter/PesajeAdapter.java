package org.transporte.carga.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.carga.application.port.out.PesajeOutService;
import org.transporte.carga.domian.bean.Pesaje;
import org.transporte.carga.infrastructure.persistence.entity.PesajeEntity;
import org.transporte.carga.infrastructure.persistence.repository.PesajeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PesajeAdapter implements PesajeOutService {

    private final PesajeRepository pesajeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Pesaje guardar(Pesaje pesaje) {
        PesajeEntity entity = modelMapper.map(pesaje, PesajeEntity.class);
        return modelMapper.map(pesajeRepository.save(entity), Pesaje.class);
    }

    @Override
    public Optional<Pesaje> buscarPorId(Long id) {
        return pesajeRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Pesaje.class));
    }

    @Override
    public List<Pesaje> listarPorViaje(Long viajeId) {
        return pesajeRepository.findByViajeId(viajeId).stream()
                .map(entity -> modelMapper.map(entity, Pesaje.class))
                .toList();
    }

    @Override
    public List<Pesaje> listarTodos() {
        return pesajeRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Pesaje.class))
                .toList();
    }
}
