package org.transporte.viajes.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "conductores", url = "${servicios.conductores.url}")
public interface ConductoresClient {

    @GetMapping("/conductores/{id}")
    ConductorResponseDto buscarPorId(@PathVariable("id") Long id);
}
