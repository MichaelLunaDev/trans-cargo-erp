package org.transporte.carga.infrastructure.controller;

import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoGuia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuiaRemisionResponseDto {

    private Long id;
    private String serie;
    private Integer numero;
    private Long viajeId;
    private String remitente;
    private String destinatario;
    private String origen;
    private String destino;
    private Long mercanciaId;
    private Double pesoTotalKg;
    private EstadoGuia estado;
    private LocalDateTime fechaEmision;
}
