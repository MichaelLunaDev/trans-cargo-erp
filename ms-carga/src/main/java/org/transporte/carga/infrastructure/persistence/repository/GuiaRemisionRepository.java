package org.transporte.carga.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.carga.infrastructure.persistence.entity.GuiaRemisionEntity;

public interface GuiaRemisionRepository extends JpaRepository<GuiaRemisionEntity, Long> {

    List<GuiaRemisionEntity> findByViajeId(Long viajeId);

    Optional<GuiaRemisionEntity> findTopBySerieOrderByNumeroDesc(String serie);
}
