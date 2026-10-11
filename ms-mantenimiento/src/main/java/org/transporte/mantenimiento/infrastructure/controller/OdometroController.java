package org.transporte.mantenimiento.infrastructure.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.mantenimiento.application.port.in.OdometroUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mantenimiento/odometros")
@RequiredArgsConstructor
public class OdometroController {

    private final OdometroUseCase odometroUseCase;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<OdometroResponseDto>> listar() {
        return ResponseEntity.ok(odometroUseCase.listarTodos().stream()
                .map(odometro -> modelMapper.map(odometro, OdometroResponseDto.class))
                .toList());
    }

    @GetMapping("/{vehiculoId}")
    public ResponseEntity<OdometroResponseDto> buscarPorVehiculo(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(modelMapper.map(odometroUseCase.buscarPorVehiculoId(vehiculoId),
                OdometroResponseDto.class));
    }
}
