package org.transporte.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

// saca el token del encabezado "Authorization: Bearer <jwt>"
@Component
public class JwtTokenConverter implements ServerAuthenticationConverter {

    private static final String PREFIJO = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String encabezado = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (encabezado == null || !encabezado.startsWith(PREFIJO) || encabezado.length() == PREFIJO.length()) {
            return Mono.empty();
        }
        String token = encabezado.substring(PREFIJO.length()).trim();
        return Mono.just(UsernamePasswordAuthenticationToken.unauthenticated(token, token));
    }
}
