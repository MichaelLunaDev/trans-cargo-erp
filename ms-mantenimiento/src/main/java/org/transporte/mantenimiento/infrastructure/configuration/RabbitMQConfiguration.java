package org.transporte.mantenimiento.infrastructure.configuration;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE = "trans.events";
    public static final String COLA_VIAJE_INICIADO = "mantenimiento.viaje.iniciado";
    public static final String ROUTING_KEY_VIAJE_INICIADO = "viaje.iniciado";

    @Bean
    public TopicExchange transEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue viajeIniciadoQueue() {
        return new Queue(COLA_VIAJE_INICIADO, true);
    }

    @Bean
    public Binding viajeIniciadoBinding(Queue viajeIniciadoQueue, TopicExchange transEventsExchange) {
        return BindingBuilder.bind(viajeIniciadoQueue).to(transEventsExchange).with(ROUTING_KEY_VIAJE_INICIADO);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public MessageRecoverer messageRecoverer() {
        return (message, causa) -> {
            log.error("Mensaje descartado tras agotar los reintentos. Cola: {}, cuerpo: {}, causa: {}",
                    message.getMessageProperties().getConsumerQueue(),
                    new String(message.getBody()),
                    causa.getCause() != null ? causa.getCause().getMessage() : causa.getMessage());
            throw new AmqpRejectAndDontRequeueException("Mensaje descartado tras agotar los reintentos", causa);
        };
    }
}
