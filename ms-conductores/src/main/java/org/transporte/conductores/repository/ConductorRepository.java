package org.transporte.conductores.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.conductores.entity.Conductor;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    Optional<Conductor> findByDni(String dni);

    boolean existsByDni(String dni);
}