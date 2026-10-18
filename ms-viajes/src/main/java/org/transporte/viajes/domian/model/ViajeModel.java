package org.transporte.viajes.domian.model;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.constraints.ViajeConstraint;
import org.transporte.viajes.domian.enums.EstadoViaje;

@Component
public class ViajeModel implements ViajeConstraint {

    @Override
    public void validarDatos(Viaje viaje) {
        if (viaje.getDistanciaKm() == null || viaje.getDistanciaKm() <= 0) {
            throw new ReglaNegocioException("La distancia del viaje debe ser mayor a cero");
        }
        if (estaVacio(viaje.getOrigen()) || estaVacio(viaje.getDestino())) {
            throw new ReglaNegocioException("El origen y el destino son obligatorios");
        }
        if (viaje.getOrigen().trim().equalsIgnoreCase(viaje.getDestino().trim())) {
            throw new ReglaNegocioException("El origen y el destino no pueden ser iguales");
        }
    }

    @Override
    public void validarTransicion(Viaje viaje, EstadoViaje nuevoEstado) {
        EstadoViaje actual = viaje.getEstado();
        boolean valida = (actual == EstadoViaje.PROGRAMADO && nuevoEstado == EstadoViaje.EN_CURSO)
                || (actual == EstadoViaje.EN_CURSO && nuevoEstado == EstadoViaje.FINALIZADO)
                || (actual == EstadoViaje.PROGRAMADO && nuevoEstado == EstadoViaje.CANCELADO);
        if (!valida) {
            throw new ReglaNegocioException("El viaje con id " + viaje.getId() + " no puede pasar de "
                    + actual + " a " + nuevoEstado);
        }
    }

    @Override
    public void validarSinViajeEnCurso(Viaje viaje, List<Viaje> enCursoDelVehiculo, List<Viaje> enCursoDelConductor) {
        if (tieneOtroViaje(viaje, enCursoDelVehiculo)) {
            throw new ReglaNegocioException("El vehiculo con id " + viaje.getVehiculoId()
                    + " ya tiene otro viaje EN_CURSO");
        }
        if (tieneOtroViaje(viaje, enCursoDelConductor)) {
            throw new ReglaNegocioException("El conductor con id " + viaje.getConductorId()
                    + " ya tiene otro viaje EN_CURSO");
        }
    }

    @Override
    public void validarEliminacion(Viaje viaje) {
        EstadoViaje actual = viaje.getEstado();
        if (actual != EstadoViaje.PROGRAMADO && actual != EstadoViaje.CANCELADO) {
            throw new ReglaNegocioException("El viaje con id " + viaje.getId() + " no se puede eliminar en estado "
                    + actual + "; solo se eliminan viajes PROGRAMADO o CANCELADO");
        }
    }

    private boolean tieneOtroViaje(Viaje viaje, List<Viaje> enCurso) {
        return enCurso.stream().anyMatch(otro -> !Objects.equals(otro.getId(), viaje.getId()));
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
