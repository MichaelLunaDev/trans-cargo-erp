package org.transporte.viajes.infrastructure.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.transporte.viajes.infrastructure.client")
public class FeignConfiguration {
}
