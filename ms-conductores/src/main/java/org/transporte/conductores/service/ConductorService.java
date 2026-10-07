package org.transporte.conductores.service;

import java.util.List;

import org.transporte.conductores.dto.ConductorRequestDto;
import org.transporte.conductores.dto.ConductorResponseDto;

public interface ConductorService {

    List<ConductorResponseDto> listar();

    ConductorResponseDto buscarPorId(Long id);

    ConductorResponseDto registrar(ConductorRequestDto request);

    ConductorResponseDto actualizar(Long id, ConductorRequestDto request);

    void eliminar(Long id);
}