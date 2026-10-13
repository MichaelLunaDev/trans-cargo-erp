package org.transporte.viajes.infrastructure.controller;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
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
public class ViajeRequestDto {

    @NotNull(message = "El vehiculo es obligatorio")
    @Positive(message = "El id del vehiculo debe ser mayor a cero")
    private Long vehiculoId;

    @NotNull(message = "El conductor es obligatorio")
    @Positive(message = "El id del conductor debe ser mayor a cero")
    private Long conductorId;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 100, message = "El origen no puede tener mas de 100 caracteres")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 100, message = "El destino no puede tener mas de 100 caracteres")
    private String destino;

    @NotNull(message = "La distancia es obligatoria")
    @Positive(message = "La distancia debe ser mayor a cero")
    private Double distanciaKm;

    @NotNull(message = "La fecha de salida es obligatoria")
    @FutureOrPresent(message = "La fecha de salida no puede estar en el pasado")
    private LocalDateTime fechaSalida;
}
