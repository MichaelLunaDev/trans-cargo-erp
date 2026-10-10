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
public class GuiaRemisionRequestDto {

    @NotBlank(message = "La serie es obligatoria")
    @Size(max = 4, message = "La serie no puede tener mas de 4 caracteres")
    private String serie;

    @NotNull(message = "El viaje es obligatorio")
    @Positive(message = "El id del viaje debe ser mayor a cero")
    private Long viajeId;

    @NotBlank(message = "El remitente es obligatorio")
    @Size(max = 150, message = "El remitente no puede tener mas de 150 caracteres")
    private String remitente;

    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 150, message = "El destinatario no puede tener mas de 150 caracteres")
    private String destinatario;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 150, message = "El origen no puede tener mas de 150 caracteres")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 150, message = "El destino no puede tener mas de 150 caracteres")
    private String destino;

    @NotNull(message = "La mercancia es obligatoria")
    @Positive(message = "El id de la mercancia debe ser mayor a cero")
    private Long mercanciaId;

    @NotNull(message = "El peso total es obligatorio")
    @Positive(message = "El peso total debe ser mayor a cero")
    private Double pesoTotalKg;
}
