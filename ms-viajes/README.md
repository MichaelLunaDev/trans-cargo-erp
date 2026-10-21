# ms-viajes

Microservicio de TRANS-CARGO ERP que programa y despacha los viajes. Antes de programar un viaje consulta
de forma sincrona (OpenFeign) a MS-Flota y MS-Conductores para validar que la unidad y el conductor esten en
regla, y al iniciar un viaje avisa de forma asincrona a MS-Mantenimiento con el evento `viaje.iniciado`.

- Puerto: `8081`
- Base de datos: `db_viajes` (MySQL, `localhost:3307`, usuario/clave `root` / `root`), tabla `viajes`
- RabbitMQ: `localhost:5672`, usuario/clave `guest`
- Arquitectura: hexagonal (`domian`, `application`, `infrastructure`)

## Servicios que consulta (OpenFeign)

| Servicio | Propiedad | Endpoint usado | Campos que usa |
|---|---|---|---|
| MS-Flota | `servicios.flota.url` (`http://localhost:8082`) | `GET /vehiculos/{id}` | `placa`, `estado`, `soatVigencia`, `citvVigencia` |
| MS-Conductores | `servicios.conductores.url` (`http://localhost:8083`) | `GET /conductores/{id}` | `estado`, `licenciaVigencia` |

Timeouts: conexion 2 s, lectura 3 s (`spring.cloud.openfeign.client.config.default.*`).
Si el servicio responde `404` se devuelve `404`; si no responde, hay timeout o responde `5xx` se devuelve `503`.

## Reglas de negocio

- `distanciaKm` mayor que 0; origen y destino obligatorios y distintos.
- Vehiculo activo (`DISPONIBLE` o `EN_RUTA`; se rechaza `EN_MANTENIMIENTO` y `DE_BAJA`), con SOAT y CITV
  vigentes en la fecha de salida (vigente = la fecha de vencimiento es igual o posterior al dia de salida).
- Conductor `HABILITADO` y con brevete (`licenciaVigencia`) vigente en la fecha de salida.
- Un vehiculo o un conductor no puede tener dos viajes `EN_CURSO` a la vez (se valida al iniciar).
- Un vehiculo o un conductor no puede tener dos viajes `PROGRAMADO`/`EN_CURSO` con la misma fecha de salida (`409`).
- Transiciones: `PROGRAMADO -> EN_CURSO -> FINALIZADO` y `PROGRAMADO -> CANCELADO`. Cualquier otra da `422`.
- La placa que devuelve MS-Flota debe tener 6 letras/numeros con guion opcional (`ABC-123` o `ABC123`).

## Evento publicado `viaje.iniciado`

Se publica **solo** al pasar un viaje a `EN_CURSO` (`PUT /api/viajes/{id}/iniciar`), despues de guardar el
cambio de estado. El contrato lo define MS-Mantenimiento (ver `ms-mantenimiento/README.md`).

| Elemento | Valor |
|---|---|
| Exchange | `trans.events` (tipo `topic`, durable; MS-Viajes lo declara) |
| Routing key | `viaje.iniciado` |
| Cola | `mantenimiento.viaje.iniciado` (la declara MS-Mantenimiento, no MS-Viajes) |
| Content-Type | `application/json` (`Jackson2JsonMessageConverter`) |
| Clase Java (`__TypeId__`) | `org.transporte.viajes.infrastructure.messaging.ViajeIniciadoEvent` |

```json
{
  "eventId": "3f1c2a9e-8b7d-4c1e-9a2f-1d2e3f4a5b6c",
  "viajeId": 10,
  "vehiculoId": 7,
  "placa": "ABC-123",
  "distanciaKm": 350.5,
  "fecha": "2026-10-10T08:30:00"
}
```

| Campo | Tipo | Origen |
|---|---|---|
| `eventId` | String (UUID) | Generado por MS-Viajes en cada publicacion |
| `viajeId` | Long | Id del viaje |
| `vehiculoId` | Long | Id del vehiculo en MS-Flota |
| `placa` | String | Placa obtenida de MS-Flota al programar el viaje |
| `distanciaKm` | Double | Distancia del viaje |
| `fecha` | String `yyyy-MM-ddTHH:mm:ss` | Fecha y hora en que se inicio el viaje |

Si RabbitMQ falla al publicar, el viaje igualmente queda `EN_CURSO` y se escribe un log `ERROR` con el
`eventId`, el `viajeId` y la causa (en ese caso MS-Mantenimiento no suma esos km).

## Endpoints

| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | `/api/viajes` | Programa un viaje (queda `PROGRAMADO`) |
| GET | `/api/viajes?estado=&vehiculoId=` | Lista viajes (filtros opcionales) |
| GET | `/api/viajes/{id}` | Busca un viaje |
| PUT | `/api/viajes/{id}/iniciar` | `PROGRAMADO -> EN_CURSO` y publica `viaje.iniciado` |
| PUT | `/api/viajes/{id}/finalizar` | `EN_CURSO -> FINALIZADO` (registra `fechaLlegada`) |
| PUT | `/api/viajes/{id}/cancelar` | `PROGRAMADO -> CANCELADO` |

Errores: `400` datos invalidos, `404` no existe (viaje, vehiculo o conductor), `409` duplicado,
`422` regla de negocio violada, `503` MS-Flota o MS-Conductores no responden.

## Como levantarlo

```bash
cd docker && docker compose up -d
cd ../ms-flota && ./mvnw spring-boot:run          # 8082
cd ../ms-conductores && ./mvnw spring-boot:run    # 8083
cd ../ms-mantenimiento && ./mvnw spring-boot:run  # 8085 (consume el evento)
cd ../ms-viajes && ./mvnw spring-boot:run         # 8081
```

```bash
curl -X POST http://localhost:8081/api/viajes -H "Content-Type: application/json" \
  -d '{"vehiculoId":1,"conductorId":1,"origen":"Lima","destino":"Arequipa","distanciaKm":1010.5,"fechaSalida":"2026-11-01T08:00:00"}'
curl -X PUT http://localhost:8081/api/viajes/1/iniciar
curl "http://localhost:8081/api/viajes?estado=EN_CURSO"
```
