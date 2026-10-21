package org.transporte.viajes.infrastructure.client;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.transporte.viajes.application.port.out.VehiculoOutService;
import org.transporte.viajes.domian.bean.VehiculoInfo;
import org.transporte.viajes.domian.model.RecursoNoEncontradoException;
import org.transporte.viajes.domian.model.ServicioNoDisponibleException;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehiculoClientAdapter implements VehiculoOutService {

    private final FlotaClient flotaClient;
    private final ModelMapper modelMapper;

    @Override
    public VehiculoInfo buscarPorId(Long id) {
        try {
            return modelMapper.map(flotaClient.buscarPorId(id), VehiculoInfo.class);
        } catch (FeignException.NotFound ex) {
            throw new RecursoNoEncontradoException("No existe el vehiculo con id " + id + " en MS-Flota");
        } catch (FeignException ex) {
            log.error("Error al consultar el vehiculo {} en MS-Flota: {}", id, ex.getMessage());
            throw new ServicioNoDisponibleException("MS-Flota no esta disponible, intente nuevamente");
        }
    }
}
