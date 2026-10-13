package org.transporte.viajes.domian.bean;

import java.io.Serializable;
import java.time.LocalDate;

import org.transporte.viajes.domian.enums.EstadoConductor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// lo minimo que MS-Viajes necesita saber de un conductor de MS-Conductores
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConductorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private EstadoConductor estado;
    private LocalDate licenciaVigencia;
}
