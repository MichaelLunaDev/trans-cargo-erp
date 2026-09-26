package org.transporte.flota.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transporte.flota.dto.VehiculoRequestDto;
import org.transporte.flota.dto.VehiculoResponseDto;
import org.transporte.flota.entity.Vehiculo;
import org.transporte.flota.exception.VehiculoNoEncontradoException;
import org.transporte.flota.repository.VehiculoRepository;
import org.transporte.flota.service.VehiculoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDto> listar() {
        return vehiculoRepository.findAll().stream()
                .map(vehiculo -> modelMapper.map(vehiculo, VehiculoResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponseDto buscarPorId(Long id) {
        return modelMapper.map(obtenerVehiculo(id), VehiculoResponseDto.class);
    }

    @Override
    @Transactional
    public VehiculoResponseDto registrar(VehiculoRequestDto request) {
        Vehiculo vehiculo = modelMapper.map(request, Vehiculo.class);
        return modelMapper.map(vehiculoRepository.save(vehiculo), VehiculoResponseDto.class);
    }

    @Override
    @Transactional
    public VehiculoResponseDto actualizar(Long id, VehiculoRequestDto request) {
        Vehiculo vehiculo = obtenerVehiculo(id);
        modelMapper.map(request, vehiculo);
        return modelMapper.map(vehiculoRepository.save(vehiculo), VehiculoResponseDto.class);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Vehiculo vehiculo = obtenerVehiculo(id);
        vehiculoRepository.delete(vehiculo);
    }

    private Vehiculo obtenerVehiculo(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNoEncontradoException("No existe el vehiculo con id " + id));
    }
}