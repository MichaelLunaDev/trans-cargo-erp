package org.transporte.mantenimiento.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "eventos_procesados")
public class EventoProcesadoEntity {

    @Id
    @Column(length = 36)
    private String eventId;

    @Column(nullable = false)
    private LocalDateTime fechaProcesado;
}
