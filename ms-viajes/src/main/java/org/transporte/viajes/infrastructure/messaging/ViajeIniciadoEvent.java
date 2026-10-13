package org.transporte.viajes.infrastructure.messaging;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// contrato definido por MS-Mantenimiento (ver ms-mantenimiento/README.md)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeIniciadoEvent {

    private String eventId;
    private Long viajeId;
    private Long vehiculoId;
    private String placa;
    private Double distanciaKm;

    // formato fijo del contrato, no depende de la configuracion del ObjectMapper
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;
}
