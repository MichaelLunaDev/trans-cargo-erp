package org.transporte.flota.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.transporte.flota.entity.EstadoVehiculo;
import org.transporte.flota.entity.Vehiculo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class VehiculoRepositoryTest {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    private Vehiculo crearVehiculo(String placa, String marca, String modelo) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setTaraKg(8500.0);
        vehiculo.setNumeroEjes(3);
        vehiculo.setSoatVigencia(LocalDate.now().plusMonths(6));
        vehiculo.setCitvVigencia(LocalDate.now().plusMonths(4));
        vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
        return vehiculo;
    }

    @BeforeEach
    void limpiar() {
        vehiculoRepository.deleteAll();
    }

    @Test
    void debeInsertarVehiculo() {
        Vehiculo guardado = vehiculoRepository.save(crearVehiculo("ABC-123", "Volvo", "FH 540"));

        assertNotNull(guardado.getId());
        assertTrue(vehiculoRepository.existsByPlaca("ABC-123"));
    }

    @Test
    void debeActualizarVehiculo() {
        Vehiculo guardado = vehiculoRepository.save(crearVehiculo("BCD-234", "Scania", "R450"));

        guardado.setEstado(EstadoVehiculo.EN_MANTENIMIENTO);
        vehiculoRepository.save(guardado);

        Vehiculo actualizado = vehiculoRepository.findByPlaca("BCD-234").orElseThrow();
        assertEquals(EstadoVehiculo.EN_MANTENIMIENTO, actualizado.getEstado());
    }

    @Test
    void debeEliminarVehiculo() {
        Vehiculo guardado = vehiculoRepository.save(crearVehiculo("CDE-345", "Mercedes-Benz", "Actros"));

        vehiculoRepository.deleteById(guardado.getId());

        assertFalse(vehiculoRepository.existsByPlaca("CDE-345"));
    }

    @Test
    void debeListarVehiculos() {
        vehiculoRepository.save(crearVehiculo("DEF-456", "Volvo", "FMX"));
        vehiculoRepository.save(crearVehiculo("EFG-567", "Iveco", "Stralis"));

        List<Vehiculo> vehiculos = vehiculoRepository.findAll();

        assertEquals(2, vehiculos.size());
    }
}