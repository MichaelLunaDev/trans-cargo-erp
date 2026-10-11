package org.transporte.mantenimiento.infrastructure.controller;

import java.time.LocalDate;

import org.transporte.mantenimiento.domian.enums.EstadoRevision;
import org.transporte.mantenimiento.domian.enums.TipoRevision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionResponseDto {

    private Long id;
    private Long vehiculoId;
    private TipoRevision tipo;
    private Double kmProgramado;
    private LocalDate fechaProgramada;
    private EstadoRevision estado;
}
