package org.transporte.mantenimiento.infrastructure.persistence.entity;

import org.transporte.mantenimiento.domian.enums.EstadoNeumatico;

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
@Table(name = "neumaticos",
        uniqueConstraints = @UniqueConstraint(columnNames = { "vehiculo_id", "posicion" }))
public class NeumaticoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vehiculoId;

    @Column(nullable = false, length = 20)
    private String posicion;

    @Column(nullable = false)
    private Double vidaUtilKm;

    @Column(nullable = false)
    private Double kmRecorridos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoNeumatico estado;
}
