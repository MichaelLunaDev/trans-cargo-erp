package org.transporte.carga.domian.bean;

import java.io.Serializable;

import org.transporte.carga.domian.enums.TipoMercancia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mercancia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String descripcion;
    private TipoMercancia tipo;
    private String claseRiesgo;
    private Double pesoKg;
}
