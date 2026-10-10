package org.transporte.carga.infrastructure.controller;

import org.transporte.carga.domian.enums.TipoMercancia;

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
public class MercanciaRequestDto {

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 150, message = "La descripcion no puede tener mas de 150 caracteres")
    private String descripcion;

    @NotNull(message = "El tipo de mercancia es obligatorio")
    private TipoMercancia tipo;

    @Size(max = 20, message = "La clase de riesgo no puede tener mas de 20 caracteres")
    private String claseRiesgo;

    @NotNull(message = "El peso es obligatorio")
    @Positive(message = "El peso debe ser mayor a cero")
    private Double pesoKg;
}
