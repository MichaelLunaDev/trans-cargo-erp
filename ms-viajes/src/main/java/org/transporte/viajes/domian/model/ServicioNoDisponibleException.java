package org.transporte.viajes.domian.model;

public class ServicioNoDisponibleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
