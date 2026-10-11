package org.transporte.mantenimiento.infrastructure.persistence.adapter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.application.port.out.EventoProcesadoOutService;
import org.transporte.mantenimiento.infrastructure.persistence.entity.EventoProcesadoEntity;
import org.transporte.mantenimiento.infrastructure.persistence.repository.EventoProcesadoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventoProcesadoAdapter implements EventoProcesadoOutService {

    private final EventoProcesadoRepository eventoProcesadoRepository;

    @Override
    public boolean existe(String eventId) {
        return eventoProcesadoRepository.existsById(eventId);
    }

    @Override
    public void registrar(String eventId) {
        eventoProcesadoRepository.save(new EventoProcesadoEntity(eventId, LocalDateTime.now()));
    }
}
