package org.transporte.mantenimiento.infrastructure.messaging;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeIniciadoEvent {

    private String eventId;
    private Long viajeId;
    private Long vehiculoId;
    private String placa;
    private Double distanciaKm;
    private LocalDateTime fecha;
}
