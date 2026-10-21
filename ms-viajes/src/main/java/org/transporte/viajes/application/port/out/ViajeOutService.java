package org.transporte.viajes.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;

public interface ViajeOutService {

    Viaje guardar(Viaje viaje);

    Optional<Viaje> buscarPorId(Long id);

    List<Viaje> listar(EstadoViaje estado, Long vehiculoId);

    List<Viaje> listarPorVehiculoYEstado(Long vehiculoId, EstadoViaje estado);

    List<Viaje> listarPorConductorYEstado(Long conductorId, EstadoViaje estado);

    boolean existePorVehiculoYFechaSalida(Long vehiculoId, LocalDateTime fechaSalida, List<EstadoViaje> estados);

    boolean existePorConductorYFechaSalida(Long conductorId, LocalDateTime fechaSalida, List<EstadoViaje> estados);

    void eliminar(Long id);
}
