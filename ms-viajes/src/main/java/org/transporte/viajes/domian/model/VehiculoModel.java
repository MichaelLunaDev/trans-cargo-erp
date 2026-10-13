package org.transporte.viajes.domian.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.transporte.viajes.domian.bean.VehiculoInfo;
import org.transporte.viajes.domian.constraints.VehiculoConstraint;
import org.transporte.viajes.domian.enums.EstadoVehiculo;

@Component
public class VehiculoModel implements VehiculoConstraint {

    @Override
    public void validarApto(VehiculoInfo vehiculo, LocalDateTime fechaSalida) {
        // en mantenimiento o de baja no puede salir; DISPONIBLE y EN_RUTA se consideran activos
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE && vehiculo.getEstado() != EstadoVehiculo.EN_RUTA) {
            throw new ReglaNegocioException("El vehiculo con id " + vehiculo.getId() + " no esta activo (estado: "
                    + vehiculo.getEstado() + ")");
        }
        LocalDate fecha = fechaSalida.toLocalDate();
        if (vehiculo.getSoatVigencia() == null || vehiculo.getSoatVigencia().isBefore(fecha)) {
            throw new ReglaNegocioException("El SOAT del vehiculo " + vehiculo.getPlaca()
                    + " no esta vigente en la fecha de salida " + fecha);
        }
        if (vehiculo.getCitvVigencia() == null || vehiculo.getCitvVigencia().isBefore(fecha)) {
            throw new ReglaNegocioException("La revision tecnica (CITV) del vehiculo " + vehiculo.getPlaca()
                    + " no esta vigente en la fecha de salida " + fecha);
        }
    }
}
