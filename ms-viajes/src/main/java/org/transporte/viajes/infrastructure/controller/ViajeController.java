package org.transporte.viajes.infrastructure.controller;

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
import org.transporte.viajes.application.port.in.ViajeUseCase;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final ViajeUseCase viajeUseCase;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ViajeResponseDto> programar(@Valid @RequestBody ViajeRequestDto request) {
        Viaje viaje = viajeUseCase.programar(modelMapper.map(request, Viaje.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(convertir(viaje));
    }

    @GetMapping
    public ResponseEntity<List<ViajeResponseDto>> listar(@RequestParam(required = false) EstadoViaje estado,
                                                         @RequestParam(required = false) Long vehiculoId) {
        return ResponseEntity.ok(viajeUseCase.listar(estado, vehiculoId).stream()
                .map(this::convertir)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(viajeUseCase.buscarPorId(id)));
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<ViajeResponseDto> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(viajeUseCase.iniciar(id)));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ViajeResponseDto> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(viajeUseCase.finalizar(id)));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ViajeResponseDto> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(convertir(viajeUseCase.cancelar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajeUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ViajeResponseDto convertir(Viaje viaje) {
        return modelMapper.map(viaje, ViajeResponseDto.class);
    }
}
