package org.transporte.mantenimiento.domian.bean;

import java.io.Serializable;

import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Neumatico implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long vehiculoId;
    private String posicion;
    private Double vidaUtilKm;
    private Double kmRecorridos;
    private EstadoNeumatico estado;
}
