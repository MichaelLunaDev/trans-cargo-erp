package org.transporte.viajes.infrastructure.client;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// respuesta de GET /conductores/{id} de MS-Conductores (solo los campos que usa MS-Viajes)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConductorResponseDto {

    private Long id;
    private String estado;
    private LocalDate licenciaVigencia;
}
