package org.transporte.viajes.infrastructure.client;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.viajes.application.port.out.ConductorOutService;
import org.transporte.viajes.domian.bean.ConductorInfo;
import org.transporte.viajes.domian.model.RecursoNoEncontradoException;
import org.transporte.viajes.domian.model.ServicioNoDisponibleException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConductorClientAdapter implements ConductorOutService {

    private final ConductoresClient conductoresClient;
    private final ModelMapper modelMapper;

    @Override
    public ConductorInfo buscarPorId(Long id) {
        try {
            return modelMapper.map(conductoresClient.buscarPorId(id), ConductorInfo.class);
        } catch (FeignException.NotFound ex) {
            throw new RecursoNoEncontradoException("No existe el conductor con id " + id + " en MS-Conductores");
        } catch (FeignException ex) {
            // sin respuesta, timeout o error 5xx: las excepciones de Feign no salen de este paquete
            log.error("Error al consultar el conductor {} en MS-Conductores: {}", id, ex.getMessage());
            throw new ServicioNoDisponibleException("MS-Conductores no esta disponible, intente nuevamente");
        }
    }
}
