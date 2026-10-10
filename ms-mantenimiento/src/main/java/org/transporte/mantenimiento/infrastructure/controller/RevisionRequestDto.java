package org.transporte.mantenimiento.infrastructure.controller;

import java.time.LocalDate;

import org.transporte.mantenimiento.domian.enums.TipoRevision;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionRequestDto {

    @NotNull(message = "El vehiculo es obligatorio")
    @Positive(message = "El id del vehiculo debe ser mayor a cero")
    private Long vehiculoId;

    @NotNull(message = "El tipo de revision es obligatorio")
    private TipoRevision tipo;

    @Positive(message = "El kilometraje programado debe ser mayor a cero")
    private Double kmProgramado;

    private LocalDate fechaProgramada;
}
