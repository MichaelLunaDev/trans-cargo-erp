package org.transporte.flota.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.transporte.flota.entity.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);
}