package org.transporte.conductores.dto;

import java.time.LocalDate;

import org.transporte.conductores.entity.CategoriaLicencia;
import org.transporte.conductores.entity.EstadoConductor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorRequestDto {

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 digitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El numero de licencia es obligatorio")
    private String licenciaNumero;

    @NotNull(message = "La categoria de licencia es obligatoria")
    private CategoriaLicencia categoriaLicencia;

    @NotNull(message = "La vigencia de la licencia es obligatoria")
    private LocalDate licenciaVigencia;

    @NotNull(message = "Los puntos acumulados son obligatorios")
    @PositiveOrZero(message = "Los puntos acumulados no pueden ser negativos")
    private Integer puntosAcumulados = 0;

    @NotNull(message = "El estado es obligatorio")
    private EstadoConductor estado;
}