package org.transporte.mantenimiento.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.mantenimiento.infrastructure.persistence.entity.NeumaticoEntity;

public interface NeumaticoRepository extends JpaRepository<NeumaticoEntity, Long> {

    List<NeumaticoEntity> findByVehiculoId(Long vehiculoId);

    Optional<NeumaticoEntity> findByVehiculoIdAndPosicion(Long vehiculoId, String posicion);
}
