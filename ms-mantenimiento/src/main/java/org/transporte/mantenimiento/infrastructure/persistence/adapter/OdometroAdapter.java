package org.transporte.mantenimiento.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.application.port.out.OdometroOutService;
import org.transporte.mantenimiento.domian.bean.Odometro;
import org.transporte.mantenimiento.infrastructure.persistence.entity.OdometroEntity;
import org.transporte.mantenimiento.infrastructure.persistence.repository.OdometroRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OdometroAdapter implements OdometroOutService {

    private final OdometroRepository odometroRepository;
    private final ModelMapper modelMapper;

    @Override
    public Odometro guardar(Odometro odometro) {
        OdometroEntity entity = modelMapper.map(odometro, OdometroEntity.class);
        return modelMapper.map(odometroRepository.save(entity), Odometro.class);
    }

    @Override
    public Optional<Odometro> buscarPorVehiculoId(Long vehiculoId) {
        return odometroRepository.findByVehiculoId(vehiculoId)
                .map(entity -> modelMapper.map(entity, Odometro.class));
    }

    @Override
    public List<Odometro> listarTodos() {
        return odometroRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Odometro.class))
                .toList();
    }
}
