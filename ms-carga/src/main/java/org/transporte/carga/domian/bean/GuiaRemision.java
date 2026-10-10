package org.transporte.carga.domian.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoGuia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuiaRemision implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String serie;
    private Integer numero;
    private Long viajeId;
    private String remitente;
    private String destinatario;
    private String origen;
    private String destino;
    private Long mercanciaId;
    private Double pesoTotalKg;
    private EstadoGuia estado;
    private LocalDateTime fechaEmision;
}
