package org.transporte.viajes.domian.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViajeIniciado implements Serializable {

    private static final long serialVersionUID = 1L;

    private String eventId;
    private Long viajeId;
    private Long vehiculoId;
    private String placa;
    private Double distanciaKm;
    private LocalDateTime fecha;
}
