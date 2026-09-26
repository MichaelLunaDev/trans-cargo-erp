package org.transporte.auth.dto;

import org.transporte.auth.entity.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String email;
    private RolUsuario rol;
    private boolean activo;
}