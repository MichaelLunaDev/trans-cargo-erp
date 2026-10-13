package org.transporte.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.sun.net.httpserver.HttpServer;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "20s")
class GatewaySeguridadTest {

    // mismo valor por defecto que jwt.secret de ms-auth
    private static final String SECRETO = "trans-cargo-erp-clave-secreta-desarrollo-minimo-32-bytes";

    // servidor falso que hace de ms-viajes y guarda el Authorization que le llega
    private static final AtomicInteger PETICIONES_RECIBIDAS = new AtomicInteger();
    private static final AtomicReference<String> AUTHORIZATION_RECIBIDO = new AtomicReference<>();
    private static HttpServer msViajesFalso;

    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    static void urlsDeServicios(DynamicPropertyRegistry registry) throws IOException {
        msViajesFalso = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        msViajesFalso.createContext("/api/viajes", exchange -> {
            PETICIONES_RECIBIDAS.incrementAndGet();
            AUTHORIZATION_RECIBIDO.set(exchange.getRequestHeaders().getFirst("Authorization"));
            byte[] cuerpo = "[]".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, cuerpo.length);
            exchange.getResponseBody().write(cuerpo);
            exchange.close();
        });
        msViajesFalso.start();

        // los demas servicios apuntan a un puerto donde no hay nadie escuchando
        String apagado = "http://localhost:" + puertoLibre();
        registry.add("servicios.viajes.url", () -> "http://localhost:" + msViajesFalso.getAddress().getPort());
        registry.add("servicios.auth.url", () -> apagado);
        registry.add("servicios.flota.url", () -> apagado);
        registry.add("servicios.conductores.url", () -> apagado);
        registry.add("servicios.carga.url", () -> apagado);
        registry.add("servicios.mantenimiento.url", () -> apagado);
        registry.add("seguridad.jwt.secret", () -> SECRETO);
    }

    @AfterAll
    static void detenerServidor() {
        msViajesFalso.stop(0);
    }

    @BeforeEach
    void limpiar() {
        PETICIONES_RECIBIDAS.set(0);
        AUTHORIZATION_RECIBIDO.set(null);
    }

    private static int puertoLibre() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    // genera el token igual que JwtService de ms-auth
    private String token(String secreto, long expiraEnMs) {
        SecretKey clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        Date ahora = new Date();
        return Jwts.builder()
                .subject("despachador1")
                .claim("userId", 1L)
                .claim("role", "DESPACHADOR")
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiraEnMs))
                .signWith(clave)
                .compact();
    }

    private void esperar401(WebTestClient.RequestHeadersSpec<?> peticion) {
        peticion.exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.status").isEqualTo(401)
                .jsonPath("$.error").isEqualTo("Unauthorized")
                .jsonPath("$.mensaje").isNotEmpty();
        assertEquals(0, PETICIONES_RECIBIDAS.get(), "el gateway no debe reenviar la peticion");
    }

    @Test
    void sinTokenDebeResponder401() {
        esperar401(webTestClient.get().uri("/api/viajes"));
    }

    @Test
    void conEncabezadoQueNoEsBearerDebeResponder401() {
        esperar401(webTestClient.get().uri("/api/viajes").header(HttpHeaders.AUTHORIZATION, "Basic abc"));
    }

    @Test
    void tokenMalFormadoDebeResponder401() {
        esperar401(webTestClient.get().uri("/api/viajes").header(HttpHeaders.AUTHORIZATION, "Bearer abc.def.ghi"));
    }

    @Test
    void tokenConFirmaIncorrectaDebeResponder401() {
        String tokenAjeno = token("otra-clave-secreta-distinta-de-la-de-ms-auth-de-56-bytes", 60_000);
        esperar401(webTestClient.get().uri("/api/viajes").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAjeno));
    }

    @Test
    void tokenExpiradoDebeResponder401() {
        String expirado = token(SECRETO, -60_000);
        webTestClient.get().uri("/api/viajes").header(HttpHeaders.AUTHORIZATION, "Bearer " + expirado)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.mensaje").isEqualTo("El token expiro");
        assertEquals(0, PETICIONES_RECIBIDAS.get());
    }

    @Test
    void tokenValidoDebeLlegarAlServicioConElAuthorization() {
        String valido = token(SECRETO, 60_000);

        webTestClient.get().uri("/api/viajes?estado=EN_CURSO").header(HttpHeaders.AUTHORIZATION, "Bearer " + valido)
                .exchange()
                .expectStatus().isOk();

        assertEquals(1, PETICIONES_RECIBIDAS.get());
        assertEquals("Bearer " + valido, AUTHORIZATION_RECIBIDO.get());
    }

    @Test
    void tokenValidoConServicioApagadoNoDebeDar401SinoErrorDeGateway() {
        String valido = token(SECRETO, 60_000);

        for (String ruta : new String[] { "/vehiculos", "/conductores/1", "/api/carga/pesajes",
                "/api/carga/guias", "/api/carga/mercancias", "/api/mantenimiento/odometros/1" }) {
            HttpStatusCode status = webTestClient.get().uri(ruta)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + valido)
                    .exchange()
                    .returnResult(String.class).getStatus();
            assertNotEquals(401, status.value(), ruta);
            assertEquals(503, status.value(), ruta + " devolvio " + status);
        }
    }

    @Test
    void loginYRegistroNoExigenToken() {
        for (String ruta : new String[] { "/auth/login", "/auth/register" }) {
            HttpStatusCode status = webTestClient.post().uri(ruta)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"username\":\"admin\",\"password\":\"12345678\"}")
                    .exchange()
                    .returnResult(String.class).getStatus();
            // pasa el filtro de seguridad; falla despues porque ms-auth no esta levantado
            assertNotEquals(401, status.value(), ruta);
            assertEquals(503, status.value(), ruta + " devolvio " + status);
        }
    }

    @Test
    void loginConTokenViejoTampocoDebeDar401() {
        HttpStatusCode status = webTestClient.post().uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token(SECRETO, -60_000))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .returnResult(String.class).getStatus();
        assertNotEquals(401, status.value());
    }

    @Test
    void otrasRutasDeAuthSiExigenToken() {
        esperar401(webTestClient.get().uri("/auth/login"));
    }
}
