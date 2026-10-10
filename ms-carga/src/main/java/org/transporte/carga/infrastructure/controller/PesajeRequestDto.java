package org.transporte.carga.infrastructure.controller;

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
public class PesajeRequestDto {

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 10, message = "La placa no puede tener mas de 10 caracteres")
    private String placa;

    @NotNull(message = "El viaje es obligatorio")
    @Positive(message = "El id del viaje debe ser mayor a cero")
    private Long viajeId;

    @NotNull(message = "La tara es obligatoria")
    @Positive(message = "La tara debe ser mayor a cero")
    private Double taraKg;

    @NotNull(message = "El peso bruto es obligatorio")
    @Positive(message = "El peso bruto debe ser mayor a cero")
    private Double pesoBrutoKg;

    @NotNull(message = "La cantidad de ejes es obligatoria")
    @Positive(message = "La cantidad de ejes debe ser mayor a cero")
    private Integer cantidadEjes;
}
