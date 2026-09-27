package org.transporte.auth.service;

import org.transporte.auth.dto.LoginRequestDto;
import org.transporte.auth.dto.LoginResponseDto;
import org.transporte.auth.dto.RegistroRequestDto;
import org.transporte.auth.dto.UsuarioResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);

    UsuarioResponseDto registrar(RegistroRequestDto request);
}