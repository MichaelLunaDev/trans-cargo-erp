package org.transporte.conductores.repository;

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
import org.transporte.conductores.entity.CategoriaLicencia;
import org.transporte.conductores.entity.Conductor;
import org.transporte.conductores.entity.EstadoConductor;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ConductorRepositoryTest {

    @Autowired
    private ConductorRepository conductorRepository;

    private Conductor crearConductor(String dni, String nombres, String apellidos) {
        Conductor conductor = new Conductor();
        conductor.setDni(dni);
        conductor.setNombres(nombres);
        conductor.setApellidos(apellidos);
        conductor.setLicenciaNumero("Q" + dni);
        conductor.setCategoriaLicencia(CategoriaLicencia.A_IIIC);
        conductor.setLicenciaVigencia(LocalDate.now().plusYears(2));
        conductor.setEstado(EstadoConductor.HABILITADO);
        return conductor;
    }

    @BeforeEach
    void limpiar() {
        conductorRepository.deleteAll();
    }

    @Test
    void debeInsertarConductor() {
        Conductor guardado = conductorRepository.save(crearConductor("45678912", "Carlos", "Quispe Mamani"));

        assertNotNull(guardado.getId());
        assertEquals(0, guardado.getPuntosAcumulados());
        assertTrue(conductorRepository.existsByDni("45678912"));
    }

    @Test
    void debeActualizarConductor() {
        Conductor guardado = conductorRepository.save(crearConductor("41237895", "Luis", "Huaman Rojas"));

        guardado.setPuntosAcumulados(25);
        guardado.setEstado(EstadoConductor.SUSPENDIDO);
        conductorRepository.save(guardado);

        Conductor actualizado = conductorRepository.findByDni("41237895").orElseThrow();
        assertEquals(25, actualizado.getPuntosAcumulados());
        assertEquals(EstadoConductor.SUSPENDIDO, actualizado.getEstado());
    }

    @Test
    void debeEliminarConductor() {
        Conductor guardado = conductorRepository.save(crearConductor("70451236", "Jorge", "Flores Diaz"));

        conductorRepository.deleteById(guardado.getId());

        assertFalse(conductorRepository.existsByDni("70451236"));
    }

    @Test
    void debeListarConductores() {
        conductorRepository.save(crearConductor("43215678", "Miguel", "Torres Vega"));
        conductorRepository.save(crearConductor("46789123", "Pedro", "Castillo Ramos"));

        List<Conductor> conductores = conductorRepository.findAll();

        assertEquals(2, conductores.size());
    }
}