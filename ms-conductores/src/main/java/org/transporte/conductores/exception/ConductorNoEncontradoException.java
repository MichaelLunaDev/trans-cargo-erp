package org.transporte.conductores.exception;

public class ConductorNoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConductorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}