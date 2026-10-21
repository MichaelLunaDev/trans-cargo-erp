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
public class VehiculoResponseDto {

    private Long id;
    private String placa;
    private String estado;
    private LocalDate soatVigencia;
    private LocalDate citvVigencia;
}
