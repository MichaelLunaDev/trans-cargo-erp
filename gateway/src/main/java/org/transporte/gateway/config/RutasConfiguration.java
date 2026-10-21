package org.transporte.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RutasConfiguration {

    @Bean
    public RouteLocator rutas(RouteLocatorBuilder builder,
                              @Value("${servicios.auth.url}") String authUrl,
                              @Value("${servicios.viajes.url}") String viajesUrl,
                              @Value("${servicios.flota.url}") String flotaUrl,
                              @Value("${servicios.conductores.url}") String conductoresUrl,
                              @Value("${servicios.carga.url}") String cargaUrl,
                              @Value("${servicios.mantenimiento.url}") String mantenimientoUrl) {
        return builder.routes()
                .route("ms-auth", r -> r.path("/auth/**").uri(authUrl))
                .route("ms-viajes", r -> r.path("/api/viajes/**").uri(viajesUrl))
                .route("ms-flota", r -> r.path("/vehiculos/**").uri(flotaUrl))
                .route("ms-conductores", r -> r.path("/conductores/**").uri(conductoresUrl))
                .route("ms-carga", r -> r.path("/api/carga/**").uri(cargaUrl))
                .route("ms-mantenimiento", r -> r.path("/api/mantenimiento/**").uri(mantenimientoUrl))
                .build();
    }
}
