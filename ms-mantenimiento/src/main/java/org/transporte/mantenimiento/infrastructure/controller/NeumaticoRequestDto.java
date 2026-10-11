package org.transporte.mantenimiento.infrastructure.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeumaticoRequestDto {

    @NotNull(message = "El vehiculo es obligatorio")
    @Positive(message = "El id del vehiculo debe ser mayor a cero")
    private Long vehiculoId;

    @NotBlank(message = "La posicion es obligatoria")
    @Size(max = 20, message = "La posicion no puede tener mas de 20 caracteres")
    private String posicion;

    @NotNull(message = "La vida util es obligatoria")
    @Positive(message = "La vida util debe ser mayor a cero")
    private Double vidaUtilKm;
}
