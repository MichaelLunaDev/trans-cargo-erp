package org.transporte.viajes.application.port.in;

import java.util.List;

import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;

public interface ViajeUseCase {

    Viaje programar(Viaje viaje);

    Viaje iniciar(Long id);

    Viaje finalizar(Long id);

    Viaje cancelar(Long id);

    Viaje buscarPorId(Long id);

    List<Viaje> listar(EstadoViaje estado, Long vehiculoId);

    void eliminar(Long id);
}
