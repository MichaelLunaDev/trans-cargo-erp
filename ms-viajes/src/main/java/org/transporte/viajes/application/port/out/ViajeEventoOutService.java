package org.transporte.viajes.application.port.out;

import org.transporte.viajes.domian.bean.ViajeIniciado;

public interface ViajeEventoOutService {

    void publicarViajeIniciado(ViajeIniciado evento);
}
