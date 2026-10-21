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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.carga.application.port.in.GuiaRemisionUseCase;
import org.transporte.carga.domian.bean.GuiaRemision;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carga/guias")
@RequiredArgsConstructor
public class GuiaRemisionController {

    private final GuiaRemisionUseCase guiaRemisionUseCase;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<GuiaRemisionResponseDto> emitir(@Valid @RequestBody GuiaRemisionRequestDto request) {
        GuiaRemision guia = guiaRemisionUseCase.emitir(modelMapper.map(request, GuiaRemision.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(guia, GuiaRemisionResponseDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaRemisionResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(guiaRemisionUseCase.buscarPorId(id), GuiaRemisionResponseDto.class));
    }

    @GetMapping
    public ResponseEntity<List<GuiaRemisionResponseDto>> listarPorViaje(@RequestParam Long viajeId) {
        return ResponseEntity.ok(guiaRemisionUseCase.listarPorViaje(viajeId).stream()
                .map(guia -> modelMapper.map(guia, GuiaRemisionResponseDto.class))
                .toList());
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<GuiaRemisionResponseDto> anular(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(guiaRemisionUseCase.anular(id), GuiaRemisionResponseDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        guiaRemisionUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
