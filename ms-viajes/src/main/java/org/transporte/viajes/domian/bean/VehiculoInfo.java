package org.transporte.viajes.domian.bean;

import java.io.Serializable;
import java.time.LocalDate;

import org.transporte.viajes.domian.enums.EstadoVehiculo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// lo minimo que MS-Viajes necesita saber de un vehiculo de MS-Flota
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String placa;
    private EstadoVehiculo estado;
    private LocalDate soatVigencia;
    private LocalDate citvVigencia;
}
