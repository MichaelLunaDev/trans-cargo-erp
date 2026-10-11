package org.transporte.mantenimiento.infrastructure.controller;

import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeumaticoResponseDto {

    private Long id;
    private Long vehiculoId;
    private String posicion;
    private Double vidaUtilKm;
    private Double kmRecorridos;
    private EstadoNeumatico estado;
}
