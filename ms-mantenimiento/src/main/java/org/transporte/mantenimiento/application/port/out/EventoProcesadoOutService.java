package org.transporte.mantenimiento.application.port.out;

public interface EventoProcesadoOutService {

    boolean existe(String eventId);

    void registrar(String eventId);
}
