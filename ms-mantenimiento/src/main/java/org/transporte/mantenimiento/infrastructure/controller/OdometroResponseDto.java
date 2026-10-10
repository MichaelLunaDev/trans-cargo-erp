package org.transporte.mantenimiento.infrastructure.controller;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OdometroResponseDto {

    private Long id;
    private Long vehiculoId;
    private String placa;
    private Double kilometrajeAcumulado;
    private LocalDateTime fechaActualizacion;
}
