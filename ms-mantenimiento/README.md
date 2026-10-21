# ms-mantenimiento

Microservicio de TRANS-CARGO ERP que controla el odometro acumulado de cada unidad, la vida util de los
neumaticos y las revisiones periodicas. Se alimenta de forma asincrona con el evento `viaje.iniciado`
que publica MS-Viajes en RabbitMQ.

- Puerto: `8085`
- Base de datos: `db_mantenimiento` (MySQL, `localhost:3307`, usuario/clave `root` / `root`)
- RabbitMQ: `localhost:5672`, usuario/clave `guest`
- Arquitectura: hexagonal (`domian`, `application`, `infrastructure`)

## Contrato del evento `ViajeIniciadoEvent`

MS-Viajes debe publicar **exactamente** este formato.

| Elemento | Valor |
|---|---|
| Exchange | `trans.events` (tipo `topic`, durable) |
| Routing key | `viaje.iniciado` |
| Cola que consume este servicio | `mantenimiento.viaje.iniciado` (durable) |
| Content-Type del mensaje | `application/json` (usar `Jackson2JsonMessageConverter` en el productor) |

Cuerpo JSON:

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

| Campo | Tipo | Regla |
|---|---|---|
| `eventId` | String (UUID) | Unico por evento. Si llega repetido se ignora (idempotencia). |
| `viajeId` | Long | Id del viaje en MS-Viajes. |
| `vehiculoId` | Long | Id del vehiculo en MS-Flota. |
| `placa` | String | 6 letras/numeros con guion opcional (`ABC-123` o `ABC123`). |
| `distanciaKm` | Double | Debe ser mayor que 0. |
| `fecha` | String ISO-8601 | Fecha y hora local, sin zona: `yyyy-MM-ddTHH:mm:ss`. |

El nombre de la clase Java del productor (cabecera `__TypeId__`) no importa: este servicio convierte el
JSON al tipo del parametro del `@RabbitListener`.

## Que hace al recibir el evento

1. Si el `eventId` ya esta en la tabla `eventos_procesados`, lo ignora.
2. Suma `distanciaKm` al odometro del vehiculo (lo crea si es su primer viaje).
3. Suma los mismos km a cada neumatico del vehiculo: al 90% de su vida util pasa a `ALERTA`, al 100% a
   `CAMBIO_REQUERIDO` (el 90% se configura con `mantenimiento.umbral-alerta`).
4. Las revisiones `PREVENTIVA` en `PENDIENTE` cuyo `kmProgramado` fue superado pasan a `VENCIDA`.
5. Guarda el `eventId` en `eventos_procesados`. Todo ocurre en una sola transaccion.

Si el procesamiento falla, el mensaje se reintenta (`spring.rabbitmq.listener.simple.retry.*`, 3 intentos
por defecto). Al agotar los reintentos se registra un log `ERROR` con el cuerpo del mensaje y se descarta.

## Endpoints

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/api/mantenimiento/odometros` | Lista los odometros |
| GET | `/api/mantenimiento/odometros/{vehiculoId}` | Odometro de un vehiculo |
| GET | `/api/mantenimiento/neumaticos?vehiculoId=` | Lista neumaticos (filtro opcional) |
| GET | `/api/mantenimiento/neumaticos/{id}` | Busca un neumatico |
| POST | `/api/mantenimiento/neumaticos` | Registra un neumatico |
| PUT | `/api/mantenimiento/neumaticos/{id}` | Actualiza un neumatico |
| PUT | `/api/mantenimiento/neumaticos/{id}/cambiar` | Cambia el neumatico (reinicia `kmRecorridos` a 0) |
| DELETE | `/api/mantenimiento/neumaticos/{id}` | Elimina un neumatico |
| POST | `/api/mantenimiento/revisiones` | Programa una revision |
| PUT | `/api/mantenimiento/revisiones/{id}/realizada` | Marca una revision como realizada |
| GET | `/api/mantenimiento/revisiones?vehiculoId=` | Revisiones de un vehiculo |
| GET | `/api/mantenimiento/revisiones/vencidas` | Revisiones vencidas |

Errores: `400` datos invalidos, `404` no existe, `409` duplicado (misma posicion de neumatico en el mismo
vehiculo), `422` regla de negocio violada.

## Como levantarlo

```bash
cd docker && docker compose up -d
cd ../ms-mantenimiento && ./mvnw spring-boot:run
```
