package org.transporte.mantenimiento.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.application.port.out.NeumaticoOutService;
import org.transporte.mantenimiento.domian.bean.Neumatico;
import org.transporte.mantenimiento.infrastructure.persistence.entity.NeumaticoEntity;
import org.transporte.mantenimiento.infrastructure.persistence.repository.NeumaticoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NeumaticoAdapter implements NeumaticoOutService {

    private final NeumaticoRepository neumaticoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Neumatico guardar(Neumatico neumatico) {
        NeumaticoEntity entity = modelMapper.map(neumatico, NeumaticoEntity.class);
        return modelMapper.map(neumaticoRepository.save(entity), Neumatico.class);
    }

    @Override
    public Optional<Neumatico> buscarPorId(Long id) {
        return neumaticoRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Neumatico.class));
    }

    @Override
    public Optional<Neumatico> buscarPorVehiculoYPosicion(Long vehiculoId, String posicion) {
        return neumaticoRepository.findByVehiculoIdAndPosicion(vehiculoId, posicion)
                .map(entity -> modelMapper.map(entity, Neumatico.class));
    }

    @Override
    public List<Neumatico> listarPorVehiculo(Long vehiculoId) {
        return neumaticoRepository.findByVehiculoId(vehiculoId).stream()
                .map(entity -> modelMapper.map(entity, Neumatico.class))
                .toList();
    }

    @Override
    public List<Neumatico> listarTodos() {
        return neumaticoRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, Neumatico.class))
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        neumaticoRepository.deleteById(id);
    }
}
