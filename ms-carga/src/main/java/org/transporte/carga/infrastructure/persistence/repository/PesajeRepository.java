package org.transporte.carga.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.carga.infrastructure.persistence.entity.PesajeEntity;

public interface PesajeRepository extends JpaRepository<PesajeEntity, Long> {

    List<PesajeEntity> findByViajeId(Long viajeId);
}
