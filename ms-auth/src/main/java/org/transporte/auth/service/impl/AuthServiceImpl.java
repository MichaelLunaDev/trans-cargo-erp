package org.transporte.auth.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.auth.dto.LoginRequestDto;
import org.transporte.auth.dto.LoginResponseDto;
import org.transporte.auth.dto.RegistroRequestDto;
import org.transporte.auth.dto.UsuarioResponseDto;
import org.transporte.auth.entity.Usuario;
import org.transporte.auth.exception.CredencialesInvalidasException;
import org.transporte.auth.exception.UsuarioDuplicadoException;
import org.transporte.auth.repository.UsuarioRepository;
import org.transporte.auth.security.JwtService;
import org.transporte.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contrasena incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Usuario o contrasena incorrectos");
        }

        String token = jwtService.generarToken(usuario);
        return new LoginResponseDto(token, "Bearer", usuario.getUsername(),
                usuario.getRol().name(), jwtService.getExpiracionSegundos());
    }

    @Override
    @Transactional
    public UsuarioResponseDto registrar(RegistroRequestDto request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new UsuarioDuplicadoException("El usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioDuplicadoException("El correo ya esta registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setRol(request.getRol());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);

        return modelMapper.map(usuarioRepository.save(usuario), UsuarioResponseDto.class);
    }
}