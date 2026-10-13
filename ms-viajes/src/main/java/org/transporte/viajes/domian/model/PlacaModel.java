package org.transporte.viajes.domian.model;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.transporte.viajes.domian.constraints.PlacaConstraint;

@Component
public class PlacaModel implements PlacaConstraint {

    private static final Pattern PLACA_PATTERN = Pattern.compile("^[A-Z0-9]{3}-?[A-Z0-9]{3}$");

    @Override
    public void validarPlaca(String placa) {
        if (placa == null || !PLACA_PATTERN.matcher(placa).matches()) {
            throw new ReglaNegocioException("La placa " + placa + " no tiene un formato valido (ejemplo: ABC-123)");
        }
    }
}
