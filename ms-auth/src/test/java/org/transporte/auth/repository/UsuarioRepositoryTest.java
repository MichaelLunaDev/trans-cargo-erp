package org.transporte.auth.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.transporte.auth.entity.RolUsuario;
import org.transporte.auth.entity.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario crearUsuario(String username, String email, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword("$2a$10$hashDePrueba");
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }

    @BeforeEach
    void limpiar() {
        usuarioRepository.deleteAll();
    }

    @Test
    void debeInsertarUsuario() {
        Usuario guardado = usuarioRepository.save(
                crearUsuario("despachador1", "d1@transcargo.pe", RolUsuario.DESPACHADOR));

        assertNotNull(guardado.getId());
        assertTrue(usuarioRepository.existsByUsername("despachador1"));
    }

    @Test
    void debeActualizarUsuario() {
        Usuario guardado = usuarioRepository.save(
                crearUsuario("mecanico1", "m1@transcargo.pe", RolUsuario.MECANICO));

        guardado.setRol(RolUsuario.ADMIN);
        usuarioRepository.save(guardado);

        Usuario actualizado = usuarioRepository.findByUsername("mecanico1").orElseThrow();
        assertEquals(RolUsuario.ADMIN, actualizado.getRol());
    }

    @Test
    void debeEliminarUsuario() {
        Usuario guardado = usuarioRepository.save(
                crearUsuario("conductor1", "c1@transcargo.pe", RolUsuario.CONDUCTOR));

        usuarioRepository.deleteById(guardado.getId());

        assertFalse(usuarioRepository.existsByUsername("conductor1"));
    }

    @Test
    void debeListarUsuarios() {
        usuarioRepository.save(crearUsuario("admin1", "a1@transcargo.pe", RolUsuario.ADMIN));
        usuarioRepository.save(crearUsuario("admin2", "a2@transcargo.pe", RolUsuario.ADMIN));

        List<Usuario> usuarios = usuarioRepository.findAll();

        assertEquals(2, usuarios.size());
    }
}