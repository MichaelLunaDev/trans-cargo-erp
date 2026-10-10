package org.transporte.carga.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import org.transporte.carga.domian.enums.EstadoPesaje;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pesajes")
public class PesajeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String placa;

    @Column(nullable = false)
    private Long viajeId;

    @Column(nullable = false)
    private Double taraKg;

    @Column(nullable = false)
    private Double pesoBrutoKg;

    @Column(nullable = false)
    private Double pesoNetoKg;

    @Column(nullable = false)
    private Integer cantidadEjes;

    @Column(nullable = false)
    private LocalDateTime fechaPesaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPesaje estado;
}
