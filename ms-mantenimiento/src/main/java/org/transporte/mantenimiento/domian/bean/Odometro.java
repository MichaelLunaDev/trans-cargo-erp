package org.transporte.mantenimiento.domian.bean;

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
public class Odometro implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long vehiculoId;
    private String placa;
    private Double kilometrajeAcumulado;
    private LocalDateTime fechaActualizacion;
}
