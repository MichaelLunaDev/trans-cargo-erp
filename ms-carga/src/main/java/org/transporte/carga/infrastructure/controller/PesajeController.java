package org.transporte.carga.infrastructure.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.transporte.carga.application.port.in.PesajeUseCase;
import org.transporte.carga.domian.bean.Pesaje;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/carga/pesajes")
@RequiredArgsConstructor
public class PesajeController {

    private final PesajeUseCase pesajeUseCase;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PesajeResponseDto> registrar(@Valid @RequestBody PesajeRequestDto request) {
        Pesaje pesaje = pesajeUseCase.registrar(modelMapper.map(request, Pesaje.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(pesaje, PesajeResponseDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PesajeResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(pesajeUseCase.buscarPorId(id), PesajeResponseDto.class));
    }

    @GetMapping
    public ResponseEntity<List<PesajeResponseDto>> listar(@RequestParam(required = false) Long viajeId) {
        List<Pesaje> pesajes = viajeId == null
                ? pesajeUseCase.listarTodos()
                : pesajeUseCase.listarPorViaje(viajeId);
        return ResponseEntity.ok(pesajes.stream()
                .map(pesaje -> modelMapper.map(pesaje, PesajeResponseDto.class))
                .toList());
    }
}
