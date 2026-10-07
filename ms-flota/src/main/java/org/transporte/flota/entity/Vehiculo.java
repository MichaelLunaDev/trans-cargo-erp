package org.transporte.flota.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(length = 50)
    private String marca;

    @Column(length = 50)
    private String modelo;

    private Double taraKg;

    private Integer numeroEjes;

    private LocalDate soatVigencia;

    private LocalDate citvVigencia;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoVehiculo estado;
}