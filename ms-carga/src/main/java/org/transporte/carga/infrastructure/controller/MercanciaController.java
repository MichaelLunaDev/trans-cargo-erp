package org.transporte.carga.infrastructure.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.transporte.carga.application.port.in.MercanciaUseCase;
import org.transporte.carga.domian.bean.Mercancia;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carga/mercancias")
@RequiredArgsConstructor
public class MercanciaController {

    private final MercanciaUseCase mercanciaUseCase;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<MercanciaResponseDto>> listar() {
        return ResponseEntity.ok(mercanciaUseCase.listar().stream()
                .map(mercancia -> modelMapper.map(mercancia, MercanciaResponseDto.class))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MercanciaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(mercanciaUseCase.buscarPorId(id), MercanciaResponseDto.class));
    }

    @PostMapping
    public ResponseEntity<MercanciaResponseDto> crear(@Valid @RequestBody MercanciaRequestDto request) {
        Mercancia mercancia = mercanciaUseCase.crear(modelMapper.map(request, Mercancia.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(mercancia, MercanciaResponseDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MercanciaResponseDto> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody MercanciaRequestDto request) {
        Mercancia mercancia = mercanciaUseCase.actualizar(id, modelMapper.map(request, Mercancia.class));
        return ResponseEntity.ok(modelMapper.map(mercancia, MercanciaResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mercanciaUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
