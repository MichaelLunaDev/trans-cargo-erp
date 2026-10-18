package org.transporte.viajes.domian.constraints;

import java.util.List;

import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.enums.EstadoViaje;

public interface ViajeConstraint {

    void validarDatos(Viaje viaje);

    void validarTransicion(Viaje viaje, EstadoViaje nuevoEstado);

    void validarSinViajeEnCurso(Viaje viaje, List<Viaje> enCursoDelVehiculo, List<Viaje> enCursoDelConductor);

    void validarEliminacion(Viaje viaje);
}
