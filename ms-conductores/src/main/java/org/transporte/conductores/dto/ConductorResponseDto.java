package org.transporte.conductores.dto;

import java.time.LocalDate;

import org.transporte.conductores.entity.CategoriaLicencia;
import org.transporte.conductores.entity.EstadoConductor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorResponseDto {

    private Long id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String licenciaNumero;
    private CategoriaLicencia categoriaLicencia;
    private LocalDate licenciaVigencia;
    private Integer puntosAcumulados;
    private EstadoConductor estado;
}