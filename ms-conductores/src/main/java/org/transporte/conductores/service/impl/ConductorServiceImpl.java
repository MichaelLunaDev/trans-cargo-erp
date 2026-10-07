package org.transporte.conductores.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.conductores.dto.ConductorRequestDto;
import org.transporte.conductores.dto.ConductorResponseDto;
import org.transporte.conductores.entity.Conductor;
import org.transporte.conductores.exception.ConductorNoEncontradoException;
import org.transporte.conductores.repository.ConductorRepository;
import org.transporte.conductores.service.ConductorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConductorServiceImpl implements ConductorService {

    private final ConductorRepository conductorRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ConductorResponseDto> listar() {
        return conductorRepository.findAll().stream()
                .map(conductor -> modelMapper.map(conductor, ConductorResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConductorResponseDto buscarPorId(Long id) {
        return modelMapper.map(obtenerConductor(id), ConductorResponseDto.class);
    }

    @Override
    @Transactional
    public ConductorResponseDto registrar(ConductorRequestDto request) {
        Conductor conductor = modelMapper.map(request, Conductor.class);
        return modelMapper.map(conductorRepository.save(conductor), ConductorResponseDto.class);
    }

    @Override
    @Transactional
    public ConductorResponseDto actualizar(Long id, ConductorRequestDto request) {
        Conductor conductor = obtenerConductor(id);
        modelMapper.map(request, conductor);
        return modelMapper.map(conductorRepository.save(conductor), ConductorResponseDto.class);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Conductor conductor = obtenerConductor(id);
        conductorRepository.delete(conductor);
    }

    private Conductor obtenerConductor(Long id) {
        return conductorRepository.findById(id)
                .orElseThrow(() -> new ConductorNoEncontradoException("No existe el conductor con id " + id));
    }
}