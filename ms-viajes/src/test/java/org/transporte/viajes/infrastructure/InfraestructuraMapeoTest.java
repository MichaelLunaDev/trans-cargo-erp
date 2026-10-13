package org.transporte.viajes.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.transporte.viajes.domian.bean.ConductorInfo;
import org.transporte.viajes.domian.bean.VehiculoInfo;
import org.transporte.viajes.domian.bean.Viaje;
import org.transporte.viajes.domian.bean.ViajeIniciado;
import org.transporte.viajes.domian.enums.EstadoConductor;
import org.transporte.viajes.domian.enums.EstadoVehiculo;
import org.transporte.viajes.domian.enums.EstadoViaje;
import org.transporte.viajes.domian.model.RecursoNoEncontradoException;
import org.transporte.viajes.domian.model.ServicioNoDisponibleException;
import org.transporte.viajes.infrastructure.client.ConductorClientAdapter;
import org.transporte.viajes.infrastructure.client.ConductorResponseDto;
import org.transporte.viajes.infrastructure.client.ConductoresClient;
import org.transporte.viajes.infrastructure.client.FlotaClient;
import org.transporte.viajes.infrastructure.client.VehiculoClientAdapter;
import org.transporte.viajes.infrastructure.client.VehiculoResponseDto;
import org.transporte.viajes.infrastructure.configuration.ModelMapperConfiguration;
import org.transporte.viajes.infrastructure.configuration.RabbitMQConfiguration;
import org.transporte.viajes.infrastructure.controller.ViajeRequestDto;
import org.transporte.viajes.infrastructure.messaging.ViajeIniciadoEvent;
import org.transporte.viajes.infrastructure.messaging.ViajeIniciadoPublisher;
import org.transporte.viajes.infrastructure.persistence.entity.ViajeEntity;

import feign.FeignException;
import feign.Request;
import feign.RetryableException;

class InfraestructuraMapeoTest {

    private final ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @Test
    void strictNoDebeCopiarVehiculoIdNiConductorIdAlId() {
        ViajeRequestDto request = new ViajeRequestDto(7L, 3L, "Lima", "Arequipa", 1010.5,
                LocalDateTime.of(2026, 11, 1, 8, 0));

        Viaje viaje = modelMapper.map(request, Viaje.class);

        assertNull(viaje.getId());
        assertEquals(7L, viaje.getVehiculoId());
        assertEquals(3L, viaje.getConductorId());
        assertEquals(1010.5, viaje.getDistanciaKm());
    }

    @Test
    void debeMapearViajeAEntidadYDeVuelta() {
        Viaje viaje = new Viaje(null, 7L, "ABC-123", 3L, "Lima", "Arequipa", 1010.5,
                LocalDateTime.of(2026, 11, 1, 8, 0), null, EstadoViaje.PROGRAMADO);

        ViajeEntity entity = modelMapper.map(viaje, ViajeEntity.class);
        assertNull(entity.getId());
        assertEquals(7L, entity.getVehiculoId());
        assertEquals(3L, entity.getConductorId());

        entity.setId(10L);
        Viaje deVuelta = modelMapper.map(entity, Viaje.class);
        assertEquals(10L, deVuelta.getId());
        assertEquals(EstadoViaje.PROGRAMADO, deVuelta.getEstado());
        assertEquals("ABC-123", deVuelta.getPlaca());
    }

    @Test
    void debeMapearRespuestasDeFlotaYConductores() {
        VehiculoInfo vehiculo = modelMapper.map(new VehiculoResponseDto(7L, "ABC-123", "DISPONIBLE",
                LocalDate.of(2027, 1, 1), LocalDate.of(2027, 2, 1)), VehiculoInfo.class);
        assertEquals(7L, vehiculo.getId());
        assertEquals(EstadoVehiculo.DISPONIBLE, vehiculo.getEstado());
        assertEquals(LocalDate.of(2027, 1, 1), vehiculo.getSoatVigencia());
        assertEquals(LocalDate.of(2027, 2, 1), vehiculo.getCitvVigencia());

        ConductorInfo conductor = modelMapper.map(new ConductorResponseDto(3L, "HABILITADO",
                LocalDate.of(2027, 3, 1)), ConductorInfo.class);
        assertEquals(3L, conductor.getId());
        assertEquals(EstadoConductor.HABILITADO, conductor.getEstado());
        assertEquals(LocalDate.of(2027, 3, 1), conductor.getLicenciaVigencia());
    }

    @Test
    void elEventoDebeSerializarseConElContratoDeMantenimiento() throws Exception {
        MessageConverter converter = new RabbitMQConfiguration()
                .jsonMessageConverter(Jackson2ObjectMapperBuilder.json().build());
        ViajeIniciadoEvent event = new ViajeIniciadoEvent("3f1c2a9e-8b7d-4c1e-9a2f-1d2e3f4a5b6c", 10L, 7L,
                "ABC-123", 350.5, LocalDateTime.of(2026, 10, 10, 8, 30));

        Message message = converter.toMessage(event, new MessageProperties());
        String json = new String(message.getBody(), StandardCharsets.UTF_8);

        assertEquals(MessageProperties.CONTENT_TYPE_JSON, message.getMessageProperties().getContentType());
        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = Jackson2ObjectMapperBuilder.json().build().readValue(json, Map.class);
        assertEquals(6, cuerpo.size());
        assertEquals("3f1c2a9e-8b7d-4c1e-9a2f-1d2e3f4a5b6c", cuerpo.get("eventId"));
        assertEquals(10, ((Number) cuerpo.get("viajeId")).intValue());
        assertEquals(7, ((Number) cuerpo.get("vehiculoId")).intValue());
        assertEquals("ABC-123", cuerpo.get("placa"));
        assertEquals(350.5, cuerpo.get("distanciaKm"));
        assertEquals("2026-10-10T08:30:00", cuerpo.get("fecha"));
    }

    @Test
    void elPublicadorDebeEnviarAlExchangeYRoutingKeyDelContrato() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        ViajeIniciadoPublisher publisher = new ViajeIniciadoPublisher(rabbitTemplate, modelMapper);

        publisher.publicarViajeIniciado(new ViajeIniciado("e-1", 10L, 7L, "ABC-123", 350.5,
                LocalDateTime.of(2026, 10, 10, 8, 30)));

        ArgumentCaptor<Object> enviado = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(eq("trans.events"), eq("viaje.iniciado"), enviado.capture());
        assertTrue(enviado.getValue() instanceof ViajeIniciadoEvent);
        ViajeIniciadoEvent event = (ViajeIniciadoEvent) enviado.getValue();
        assertEquals("e-1", event.getEventId());
        assertEquals(10L, event.getViajeId());
        assertEquals(7L, event.getVehiculoId());
        assertEquals(350.5, event.getDistanciaKm());
    }

    @Test
    void el404DeFlotaSeTraduceANoEncontradoYLaCaidaANoDisponible() {
        FlotaClient flotaClient = mock(FlotaClient.class);
        VehiculoClientAdapter adapter = new VehiculoClientAdapter(flotaClient, modelMapper);
        when(flotaClient.buscarPorId(1L)).thenThrow(new FeignException.NotFound("no existe", peticion(), null, null));
        when(flotaClient.buscarPorId(2L)).thenThrow(mock(RetryableException.class));
        when(flotaClient.buscarPorId(3L)).thenThrow(
                new FeignException.InternalServerError("error", peticion(), null, null));

        assertThrows(RecursoNoEncontradoException.class, () -> adapter.buscarPorId(1L));
        assertThrows(ServicioNoDisponibleException.class, () -> adapter.buscarPorId(2L));
        assertThrows(ServicioNoDisponibleException.class, () -> adapter.buscarPorId(3L));
    }

    @Test
    void el404DeConductoresSeTraduceANoEncontradoYLaCaidaANoDisponible() {
        ConductoresClient conductoresClient = mock(ConductoresClient.class);
        ConductorClientAdapter adapter = new ConductorClientAdapter(conductoresClient, modelMapper);
        when(conductoresClient.buscarPorId(1L))
                .thenThrow(new FeignException.NotFound("no existe", peticion(), null, null));
        when(conductoresClient.buscarPorId(2L)).thenThrow(mock(RetryableException.class));

        assertThrows(RecursoNoEncontradoException.class, () -> adapter.buscarPorId(1L));
        assertThrows(ServicioNoDisponibleException.class, () -> adapter.buscarPorId(2L));
    }

    private Request peticion() {
        return Request.create(Request.HttpMethod.GET, "http://localhost", Map.of(), null, StandardCharsets.UTF_8,
                null);
    }
}
