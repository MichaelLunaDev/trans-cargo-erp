package org.transporte.viajes.domian.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.transporte.viajes.domian.bean.ConductorInfo;
import org.transporte.viajes.domian.constraints.ConductorConstraint;
import org.transporte.viajes.domian.enums.EstadoConductor;

@Component
public class ConductorModel implements ConductorConstraint {

    @Override
    public void validarApto(ConductorInfo conductor, LocalDateTime fechaSalida) {
        if (conductor.getEstado() != EstadoConductor.HABILITADO) {
            throw new ReglaNegocioException("El conductor con id " + conductor.getId() + " no esta activo (estado: "
                    + conductor.getEstado() + ")");
        }
        LocalDate fecha = fechaSalida.toLocalDate();
        if (conductor.getLicenciaVigencia() == null || conductor.getLicenciaVigencia().isBefore(fecha)) {
            throw new ReglaNegocioException("El brevete del conductor con id " + conductor.getId()
                    + " no esta vigente en la fecha de salida " + fecha);
        }
    }
}
