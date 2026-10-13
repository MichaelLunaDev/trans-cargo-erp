package org.transporte.viajes.domian.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.transporte.viajes.domian.enums.EstadoViaje;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Viaje implements Serializable {

    private static final long serialVersionUID = 1L;

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
