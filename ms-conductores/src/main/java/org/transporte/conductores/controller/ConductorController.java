package org.transporte.conductores.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.conductores.dto.ConductorRequestDto;
import org.transporte.conductores.dto.ConductorResponseDto;
import org.transporte.conductores.service.ConductorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/conductores")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorService conductorService;

    @GetMapping
    public ResponseEntity<List<ConductorResponseDto>> listar() {
        return ResponseEntity.ok(conductorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(conductorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ConductorResponseDto> registrar(@Valid @RequestBody ConductorRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(conductorService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConductorResponseDto> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody ConductorRequestDto request) {
        return ResponseEntity.ok(conductorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        conductorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}