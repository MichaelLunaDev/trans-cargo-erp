package org.transporte.viajes.infrastructure.persistence.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.viajes.application.port.out.ViajeOutService;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;
import org.transporte.viajes.infrastructure.persistence.entity.ViajeEntity;
import org.transporte.viajes.infrastructure.persistence.repository.ViajeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ViajeAdapter implements ViajeOutService {

    private final ViajeRepository viajeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Viaje guardar(Viaje viaje) {
        ViajeEntity entity = modelMapper.map(viaje, ViajeEntity.class);
        return modelMapper.map(viajeRepository.save(entity), Viaje.class);
    }

    @Override
    public Optional<Viaje> buscarPorId(Long id) {
        return viajeRepository.findById(id)
                .map(entity -> modelMapper.map(entity, Viaje.class));
    }

    @Override
    public List<Viaje> listar(EstadoViaje estado, Long vehiculoId) {
        List<ViajeEntity> entities;
        if (estado != null && vehiculoId != null) {
            entities = viajeRepository.findByVehiculoIdAndEstado(vehiculoId, estado);
        } else if (estado != null) {
            entities = viajeRepository.findByEstado(estado);
        } else if (vehiculoId != null) {
            entities = viajeRepository.findByVehiculoId(vehiculoId);
        } else {
            entities = viajeRepository.findAll();
        }
        return convertir(entities);
    }

    @Override
    public List<Viaje> listarPorVehiculoYEstado(Long vehiculoId, EstadoViaje estado) {
        return convertir(viajeRepository.findByVehiculoIdAndEstado(vehiculoId, estado));
    }

    @Override
    public List<Viaje> listarPorConductorYEstado(Long conductorId, EstadoViaje estado) {
        return convertir(viajeRepository.findByConductorIdAndEstado(conductorId, estado));
    }

    @Override
    public boolean existePorVehiculoYFechaSalida(Long vehiculoId, LocalDateTime fechaSalida,
            List<EstadoViaje> estados) {
        return viajeRepository.existsByVehiculoIdAndFechaSalidaAndEstadoIn(vehiculoId, fechaSalida, estados);
    }

    @Override
    public boolean existePorConductorYFechaSalida(Long conductorId, LocalDateTime fechaSalida,
            List<EstadoViaje> estados) {
        return viajeRepository.existsByConductorIdAndFechaSalidaAndEstadoIn(conductorId, fechaSalida, estados);
    }

    private List<Viaje> convertir(List<ViajeEntity> entities) {
        return entities.stream()
                .map(entity -> modelMapper.map(entity, Viaje.class))
                .toList();
    }
}
