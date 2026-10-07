package org.transporte.conductores.entity;

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
@Table(name = "conductores")
public class Conductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(length = 80)
    private String nombres;

    @Column(length = 80)
    private String apellidos;

    @Column(unique = true, length = 20)
    private String licenciaNumero;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private CategoriaLicencia categoriaLicencia;

    private LocalDate licenciaVigencia;

    @Column(nullable = false)
    private Integer puntosAcumulados = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoConductor estado;
}