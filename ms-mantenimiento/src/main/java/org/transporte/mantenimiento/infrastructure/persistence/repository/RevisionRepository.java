package org.transporte.mantenimiento.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.infrastructure.persistence.entity.RevisionEntity;

public interface RevisionRepository extends JpaRepository<RevisionEntity, Long> {

    List<RevisionEntity> findByVehiculoId(Long vehiculoId);

    List<RevisionEntity> findByVehiculoIdAndEstado(Long vehiculoId, EstadoRevision estado);

    List<RevisionEntity> findByEstado(EstadoRevision estado);
}
