package org.transporte.gateway.config;

import java.net.ConnectException;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class ServicioNoDisponibleFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorMap(ConnectException.class, ex -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "El servicio destino no esta disponible", ex));
    }
}
