package org.transporte.auth.exception;

public class CredencialesInvalidasException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}