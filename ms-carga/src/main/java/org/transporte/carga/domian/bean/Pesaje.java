package org.transporte.carga.domian.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoPesaje;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pesaje implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String placa;
    private Long viajeId;
    private Double taraKg;
    private Double pesoBrutoKg;
    private Double pesoNetoKg;
    private Integer cantidadEjes;
    private LocalDateTime fechaPesaje;
    private EstadoPesaje estado;
}
