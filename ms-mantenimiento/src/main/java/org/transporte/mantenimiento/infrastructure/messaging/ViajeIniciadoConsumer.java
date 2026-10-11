package org.transporte.mantenimiento.infrastructure.messaging;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.transporte.mantenimiento.application.port.in.RegistrarViajeUseCase;
import org.transporte.mantenimiento.domian.bean.ViajeIniciado;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViajeIniciadoConsumer {

    private final RegistrarViajeUseCase registrarViajeUseCase;
    private final ModelMapper modelMapper;

    @RabbitListener(queues = "mantenimiento.viaje.iniciado")
    public void recibir(ViajeIniciadoEvent event) {
        log.info("Evento viaje.iniciado recibido: eventId={}, viajeId={}", event.getEventId(), event.getViajeId());
        registrarViajeUseCase.registrar(modelMapper.map(event, ViajeIniciado.class));
    }
}
