package org.transporte.mantenimiento.application.port.in;

import org.transporte.mantenimiento.domian.bean.ViajeIniciado;

public interface RegistrarViajeUseCase {

    void registrar(ViajeIniciado viaje);
}
