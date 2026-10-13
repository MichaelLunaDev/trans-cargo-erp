package org.transporte.gateway.security;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtValidador jwtValidador;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        return Mono.fromCallable(() -> jwtValidador.validar(token))
                .map(claims -> crearAutenticacion(claims, token))
                .onErrorMap(ExpiredJwtException.class, ex -> new BadCredentialsException("El token expiro", ex))
                .onErrorMap(ex -> ex instanceof JwtException || ex instanceof IllegalArgumentException,
                        ex -> new BadCredentialsException("El token no es valido", ex));
    }

    private Authentication crearAutenticacion(Claims claims, String token) {
        // ms-auth envia el rol en el claim "role"; por ahora solo se exige estar autenticado
        String rol = claims.get("role", String.class);
        List<SimpleGrantedAuthority> permisos = rol == null ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + rol));
        return UsernamePasswordAuthenticationToken.authenticated(claims.getSubject(), token, permisos);
    }
}
