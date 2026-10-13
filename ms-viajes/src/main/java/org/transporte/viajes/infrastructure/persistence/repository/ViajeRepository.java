package org.transporte.viajes.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.viajes.domian.enums.EstadoViaje;
import org.transporte.viajes.infrastructure.persistence.entity.ViajeEntity;

public interface ViajeRepository extends JpaRepository<ViajeEntity, Long> {

    List<ViajeEntity> findByEstado(EstadoViaje estado);

    List<ViajeEntity> findByVehiculoId(Long vehiculoId);

    List<ViajeEntity> findByVehiculoIdAndEstado(Long vehiculoId, EstadoViaje estado);

    List<ViajeEntity> findByConductorIdAndEstado(Long conductorId, EstadoViaje estado);

    boolean existsByVehiculoIdAndFechaSalidaAndEstadoIn(Long vehiculoId, LocalDateTime fechaSalida,
            Collection<EstadoViaje> estados);

    boolean existsByConductorIdAndFechaSalidaAndEstadoIn(Long conductorId, LocalDateTime fechaSalida,
            Collection<EstadoViaje> estados);
}
