package org.transporte.flota.dto;

import java.time.LocalDate;

import org.transporte.flota.entity.EstadoVehiculo;

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
public class VehiculoRequestDto {

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 10, message = "La placa no puede tener mas de 10 caracteres")
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "La tara es obligatoria")
    @Positive(message = "La tara debe ser mayor a cero")
    private Double taraKg;

    @NotNull(message = "El numero de ejes es obligatorio")
    @Positive(message = "El numero de ejes debe ser mayor a cero")
    private Integer numeroEjes;

    @NotNull(message = "La vigencia del SOAT es obligatoria")
    private LocalDate soatVigencia;

    @NotNull(message = "La vigencia del CITV es obligatoria")
    private LocalDate citvVigencia;

    @NotNull(message = "El estado es obligatorio")
    private EstadoVehiculo estado;
}