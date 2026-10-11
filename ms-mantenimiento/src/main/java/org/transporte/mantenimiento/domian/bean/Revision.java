package org.transporte.mantenimiento.domian.bean;

import java.io.Serializable;
import java.time.LocalDate;

import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Revision implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long vehiculoId;
    private TipoRevision tipo;
    private Double kmProgramado;
    private LocalDate fechaProgramada;
    private EstadoRevision estado;
}
