package org.transporte.carga.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoGuia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guias_remision", uniqueConstraints = @UniqueConstraint(columnNames = { "serie", "numero" }))
public class GuiaRemisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String serie;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private Long viajeId;

    @Column(nullable = false, length = 150)
    private String remitente;

    @Column(nullable = false, length = 150)
    private String destinatario;

    @Column(nullable = false, length = 150)
    private String origen;

    @Column(nullable = false, length = 150)
    private String destino;

    @Column(nullable = false)
    private Long mercanciaId;

    @Column(nullable = false)
    private Double pesoTotalKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoGuia estado;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;
}
