package org.transporte.mantenimiento.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.mantenimiento.infrastructure.persistence.entity.EventoProcesadoEntity;

public interface EventoProcesadoRepository extends JpaRepository<EventoProcesadoEntity, String> {
}
