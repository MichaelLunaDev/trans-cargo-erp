package org.transporte.flota.service;

import java.util.List;

import org.transporte.flota.dto.VehiculoRequestDto;
import org.transporte.flota.dto.VehiculoResponseDto;

public interface VehiculoService {

    List<VehiculoResponseDto> listar();

    VehiculoResponseDto buscarPorId(Long id);

    VehiculoResponseDto registrar(VehiculoRequestDto request);

    VehiculoResponseDto actualizar(Long id, VehiculoRequestDto request);

    void eliminar(Long id);
}