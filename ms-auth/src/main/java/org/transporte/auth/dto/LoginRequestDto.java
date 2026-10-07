package org.transporte.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "El usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
}