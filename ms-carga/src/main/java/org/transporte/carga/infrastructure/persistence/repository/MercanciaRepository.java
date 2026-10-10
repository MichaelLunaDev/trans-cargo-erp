package org.transporte.carga.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.carga.infrastructure.persistence.entity.MercanciaEntity;

public interface MercanciaRepository extends JpaRepository<MercanciaEntity, Long> {

    boolean existsByDescripcion(String descripcion);
}
