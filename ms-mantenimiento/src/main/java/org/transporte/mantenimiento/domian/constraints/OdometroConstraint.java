package org.transporte.mantenimiento.domian.constraints;

public interface OdometroConstraint {

    void validarDistancia(Double distanciaKm);

    Double sumarKilometraje(Double kilometrajeAcumulado, Double distanciaKm);
}
