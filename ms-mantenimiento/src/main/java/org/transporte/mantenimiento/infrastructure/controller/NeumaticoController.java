package org.transporte.mantenimiento.infrastructure.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.mantenimiento.application.port.in.NeumaticoUseCase;
import org.transporte.mantenimiento.domian.bean.Neumatico;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mantenimiento/neumaticos")
@RequiredArgsConstructor
public class NeumaticoController {

    private final NeumaticoUseCase neumaticoUseCase;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<NeumaticoResponseDto>> listar(@RequestParam(required = false) Long vehiculoId) {
        List<Neumatico> neumaticos = vehiculoId == null
                ? neumaticoUseCase.listarTodos()
                : neumaticoUseCase.listarPorVehiculo(vehiculoId);
        return ResponseEntity.ok(neumaticos.stream()
                .map(neumatico -> modelMapper.map(neumatico, NeumaticoResponseDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeumaticoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(neumaticoUseCase.buscarPorId(id), NeumaticoResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<NeumaticoResponseDto> registrar(@Valid @RequestBody NeumaticoRequestDto request) {
        Neumatico neumatico = neumaticoUseCase.registrar(modelMapper.map(request, Neumatico.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(neumatico, NeumaticoResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NeumaticoResponseDto> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody NeumaticoRequestDto request) {
        Neumatico neumatico = neumaticoUseCase.actualizar(id, modelMapper.map(request, Neumatico.class));
        return ResponseEntity.ok(modelMapper.map(neumatico, NeumaticoResponseDto.class));
    }

    @PutMapping("/{id}/cambiar")
    public ResponseEntity<NeumaticoResponseDto> cambiar(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(neumaticoUseCase.cambiar(id), NeumaticoResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        neumaticoUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
