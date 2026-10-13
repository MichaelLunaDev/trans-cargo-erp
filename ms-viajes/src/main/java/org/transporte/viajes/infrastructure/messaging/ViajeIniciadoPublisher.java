package org.transporte.viajes.infrastructure.messaging;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.transporte.viajes.application.port.out.ViajeEventoOutService;
import org.transporte.viajes.domian.bean.ViajeIniciado;
import org.transporte.viajes.infrastructure.configuration.RabbitMQConfiguration;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ViajeIniciadoPublisher implements ViajeEventoOutService {

    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;

    @Override
    public void publicarViajeIniciado(ViajeIniciado evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE,
                RabbitMQConfiguration.ROUTING_KEY_VIAJE_INICIADO,
                modelMapper.map(evento, ViajeIniciadoEvent.class));
    }
}
