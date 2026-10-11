package org.transporte.mantenimiento.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.mantenimiento.infrastructure.persistence.entity.OdometroEntity;

public interface OdometroRepository extends JpaRepository<OdometroEntity, Long> {

    Optional<OdometroEntity> findByVehiculoId(Long vehiculoId);
}
