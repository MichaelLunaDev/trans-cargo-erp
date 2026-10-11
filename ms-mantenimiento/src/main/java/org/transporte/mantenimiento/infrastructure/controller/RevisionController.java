package org.transporte.mantenimiento.infrastructure.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.mantenimiento.application.port.in.RevisionUseCase;
import org.transporte.mantenimiento.domian.bean.Revision;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mantenimiento/revisiones")
@RequiredArgsConstructor
public class RevisionController {

    private final RevisionUseCase revisionUseCase;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<RevisionResponseDto> programar(@Valid @RequestBody RevisionRequestDto request) {
        Revision revision = revisionUseCase.programar(modelMapper.map(request, Revision.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(revision, RevisionResponseDto.class));
    }

    @PutMapping("/{id}/realizada")
    public ResponseEntity<RevisionResponseDto> marcarRealizada(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(revisionUseCase.marcarRealizada(id), RevisionResponseDto.class));
    }

    @GetMapping
    public ResponseEntity<List<RevisionResponseDto>> listarPorVehiculo(@RequestParam Long vehiculoId) {
        return ResponseEntity.ok(convertir(revisionUseCase.listarPorVehiculo(vehiculoId)));
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<RevisionResponseDto>> listarVencidas() {
        return ResponseEntity.ok(convertir(revisionUseCase.listarVencidas()));
    }

    private List<RevisionResponseDto> convertir(List<Revision> revisiones) {
        return revisiones.stream()
                .map(revision -> modelMapper.map(revision, RevisionResponseDto.class))
                .toList();
    }
}
