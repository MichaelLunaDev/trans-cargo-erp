package org.transporte.viajes.infrastructure.controller;

import java.time.LocalDateTime;

import org.transporte.viajes.domian.enums.EstadoViaje;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeResponseDto {

    private Long id;
    private Long vehiculoId;
    private String placa;
    private Long conductorId;
    private String origen;
    private String destino;
    private Double distanciaKm;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private EstadoViaje estado;
}
