package org.transporte.mantenimiento.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.transporte.mantenimiento.domian.bean.Neumatico;
import org.transporte.mantenimiento.infrastructure.configuration.ModelMapperConfiguration;
import org.transporte.mantenimiento.infrastructure.configuration.RabbitMQConfiguration;
import org.transporte.mantenimiento.infrastructure.controller.NeumaticoRequestDto;
import org.transporte.mantenimiento.infrastructure.messaging.ViajeIniciadoEvent;

class InfraestructuraMapeoTest {

    private final ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @Test
    void elRequestNoDebeLlenarElIdConElVehiculoId() {
        Neumatico neumatico = modelMapper.map(new NeumaticoRequestDto(7L, "DEL-IZQ", 100_000.0), Neumatico.class);

        assertNull(neumatico.getId());
        assertEquals(7L, neumatico.getVehiculoId());
    }

    @Test
    void debeLeerElEventoJsonQuePublicaMsViajes() {
        MessageConverter converter = new RabbitMQConfiguration()
                .jsonMessageConverter(Jackson2ObjectMapperBuilder.json().build());
        String json = """
                {"eventId":"3f1c2a9e-8b7d-4c1e-9a2f-1d2e3f4a5b6c","viajeId":10,"vehiculoId":7,
                 "placa":"ABC-123","distanciaKm":350.5,"fecha":"2026-10-10T08:30:00"}
                """;
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setHeader("__TypeId__", "org.transporte.viajes.infrastructure.messaging.ViajeIniciadoEvent");
        properties.setInferredArgumentType(ViajeIniciadoEvent.class);

        ViajeIniciadoEvent event = (ViajeIniciadoEvent) converter
                .fromMessage(new Message(json.getBytes(StandardCharsets.UTF_8), properties));

        assertEquals("3f1c2a9e-8b7d-4c1e-9a2f-1d2e3f4a5b6c", event.getEventId());
        assertEquals(7L, event.getVehiculoId());
        assertEquals(350.5, event.getDistanciaKm());
        assertEquals(LocalDateTime.of(2026, 10, 10, 8, 30), event.getFecha());
    }
}
