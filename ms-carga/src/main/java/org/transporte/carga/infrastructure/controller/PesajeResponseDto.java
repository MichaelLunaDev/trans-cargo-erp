package org.transporte.carga.infrastructure.controller;

import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoPesaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PesajeResponseDto {

    private Long id;
    private String placa;
    private Long viajeId;
    private Double taraKg;
    private Double pesoBrutoKg;
    private Double pesoNetoKg;
    private Integer cantidadEjes;
    private LocalDateTime fechaPesaje;
    private EstadoPesaje estado;
}
