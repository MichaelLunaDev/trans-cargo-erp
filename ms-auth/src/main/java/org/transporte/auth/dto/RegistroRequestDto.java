package org.transporte.auth.dto;

import org.transporte.auth.entity.RolUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequestDto {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 8, max = 60, message = "La contrasena debe tener entre 8 y 60 caracteres")
    private String password;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}