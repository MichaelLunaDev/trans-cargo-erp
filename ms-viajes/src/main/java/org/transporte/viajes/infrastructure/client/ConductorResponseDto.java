package org.transporte.viajes.infrastructure.client;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConductorResponseDto {

    private Long id;
    private String estado;
    private LocalDate licenciaVigencia;
}
