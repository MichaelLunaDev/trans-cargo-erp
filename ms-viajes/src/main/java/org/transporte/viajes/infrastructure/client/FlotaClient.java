package org.transporte.viajes.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "flota", url = "${servicios.flota.url}")
public interface FlotaClient {

    @GetMapping("/vehiculos/{id}")
    VehiculoResponseDto buscarPorId(@PathVariable("id") Long id);
}
