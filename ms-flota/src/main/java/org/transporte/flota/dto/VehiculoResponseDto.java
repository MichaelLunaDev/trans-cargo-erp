package org.transporte.flota.dto;

import java.time.LocalDate;

import org.transporte.flota.entity.EstadoVehiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoResponseDto {

    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Double taraKg;
    private Integer numeroEjes;
    private LocalDate soatVigencia;
    private LocalDate citvVigencia;
    private EstadoVehiculo estado;
}