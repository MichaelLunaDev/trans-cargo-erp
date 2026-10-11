package org.transporte.mantenimiento.domian.model;

import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.domian.constraints.OdometroConstraint;

@Component
public class OdometroModel implements OdometroConstraint {

    @Override
    public void validarDistancia(Double distanciaKm) {
        if (distanciaKm == null || distanciaKm <= 0) {
            throw new ReglaNegocioException("La distancia del viaje debe ser mayor a cero");
        }
    }

    @Override
    public Double sumarKilometraje(Double kilometrajeAcumulado, Double distanciaKm) {
        validarDistancia(distanciaKm);
        double actual = kilometrajeAcumulado == null ? 0 : kilometrajeAcumulado;
        return actual + distanciaKm;
    }
}
