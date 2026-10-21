package org.transporte.mantenimiento.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.application.port.out.RevisionOutService;
import org.transporte.mantenimiento.domian.bean.Revision;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.infrastructure.persistence.entity.RevisionEntity;
import org.transporte.mantenimiento.infrastructure.persistence.repository.RevisionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RevisionAdapter implements RevisionOutService {

    private final RevisionRepository revisionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Revision guardar(Revision revision) {
        RevisionEntity entity = modelMapper.map(revision, RevisionEntity.class);
        return modelMapper.map(revisionRepository.save(entity), Revision.class);
    }

    @Override
    public Optional<Revision> buscarPorId(Long id) {
        return revisionRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Revision.class));
    }

    @Override
    public List<Revision> listarPorVehiculo(Long vehiculoId) {
        return revisionRepository.findByVehiculoId(vehiculoId).stream()
                .map(entity -> modelMapper.map(entity, Revision.class))
                .toList();
    }

    @Override
    public List<Revision> listarPorVehiculoYEstado(Long vehiculoId, EstadoRevision estado) {
        return revisionRepository.findByVehiculoIdAndEstado(vehiculoId, estado).stream()
                .map(entity -> modelMapper.map(entity, Revision.class))
                .toList();
    }

    @Override
    public List<Revision> listarPorEstado(EstadoRevision estado) {
        return revisionRepository.findByEstado(estado).stream()
                .map(entity -> modelMapper.map(entity, Revision.class))
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        revisionRepository.deleteById(id);
    }
}
