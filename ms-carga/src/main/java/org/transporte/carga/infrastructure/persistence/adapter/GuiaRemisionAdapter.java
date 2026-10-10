package org.transporte.carga.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.carga.application.port.out.GuiaRemisionOutService;
import org.transporte.carga.domian.bean.GuiaRemision;
import org.transporte.carga.infrastructure.persistence.entity.GuiaRemisionEntity;
import org.transporte.carga.infrastructure.persistence.repository.GuiaRemisionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GuiaRemisionAdapter implements GuiaRemisionOutService {

    private final GuiaRemisionRepository guiaRemisionRepository;
    private final ModelMapper modelMapper;

    @Override
    public GuiaRemision guardar(GuiaRemision guiaRemision) {
        GuiaRemisionEntity entity = modelMapper.map(guiaRemision, GuiaRemisionEntity.class);
        return modelMapper.map(guiaRemisionRepository.save(entity), GuiaRemision.class);
    }

    @Override
    public Optional<GuiaRemision> buscarPorId(Long id) {
        return guiaRemisionRepository.findById(id)
                .map(entity -> modelMapper.map(entity, GuiaRemision.class));
    }

    @Override
    public List<GuiaRemision> listarPorViaje(Long viajeId) {
        return guiaRemisionRepository.findByViajeId(viajeId).stream()
                .map(entity -> modelMapper.map(entity, GuiaRemision.class))
                .toList();
    }

    @Override
    public Integer obtenerUltimoNumero(String serie) {
        return guiaRemisionRepository.findTopBySerieOrderByNumeroDesc(serie)
                .map(GuiaRemisionEntity::getNumero)
                .orElse(0);
    }
}
