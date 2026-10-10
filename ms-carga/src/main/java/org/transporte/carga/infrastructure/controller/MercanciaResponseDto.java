package org.transporte.carga.infrastructure.controller;

import org.transporte.carga.domian.enums.TipoMercancia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MercanciaResponseDto {

    private Long id;
    private String descripcion;
    private TipoMercancia tipo;
    private String claseRiesgo;
    private Double pesoKg;
}
