# gateway

El gateway es la puerta de entrada de TRANS-CARGO ERP. Todas las peticiones pasan primero por aquí, en el
puerto `8080`. El gateway revisa si traes un token válido y, si todo está bien, manda tu petición al
microservicio que corresponde.

Piensa en él como el portero de un edificio: te pide tu credencial (el token), la revisa y te deja pasar a
la oficina correcta. Si no tienes credencial o está vencida, no pasas.

- Puerto: `8080`
- No usa base de datos.

## ¿A dónde va cada ruta?

Las rutas llegan al servicio tal cual, sin cambiarles nada.

| Si llamas a... | El gateway la manda a... |
|---|---|
| `/auth/**` | ms-auth (`8086`) |
| `/api/viajes/**` | ms-viajes (`8081`) |
| `/vehiculos/**` | ms-flota (`8082`) |
| `/conductores/**` | ms-conductores (`8083`) |
| `/api/carga/**` (pesajes, guías y mercancías) | ms-carga (`8084`) |
| `/api/mantenimiento/**` (odómetros, neumáticos y revisiones) | ms-mantenimiento (`8085`) |

Las direcciones de cada servicio están en `application.properties` (`servicios.viajes.url`, etc.). Si un
servicio cambia de puerto, solo cambias esa línea.

## ¿Qué rutas no piden token?

Solo dos:

- `POST /auth/login` (iniciar sesión)
- `POST /auth/register` (crear usuario)

Todo lo demás pide el token.

## ¿Cómo se revisa el token?

1. Primero inicias sesión en `/auth/login`. ms-auth te devuelve un token.
2. En cada petición mandas ese token en el encabezado: `Authorization: Bearer <token>`.
3. El gateway revisa dos cosas:
   - Que la firma sea correcta, o sea, que el token lo hizo ms-auth y nadie lo cambió.
   - Que no esté vencido. Los tokens duran 1 hora.
4. Si está bien, la petición sigue su camino al servicio. El encabezado `Authorization` se manda tal cual.
5. Si está mal, el gateway responde `401` y la petición no llega a ningún servicio.

Para revisar la firma, el gateway usa la misma clave secreta que ms-auth. Está en `application.properties`
como `seguridad.jwt.secret`. **Las dos claves tienen que ser iguales.** Si cambias una, cambia la otra.
En los dos servicios se puede cambiar con la variable de entorno `JWT_SECRET`.

Por ahora solo se pide que el token sea válido. No se revisa el rol del usuario (ADMIN, DESPACHADOR,
etc.). Eso queda para más adelante.

## ¿Qué respuestas puedes recibir del gateway?

| Código | Qué pasó |
|---|---|
| `401` | No mandaste token, el token está mal o ya venció. |
| `503` | El servicio al que querías llegar está apagado. |
| `504` | El servicio tardó demasiado en responder. |

Cuando es `401`, la respuesta viene así:

```json
{ "status": 401, "error": "Unauthorized", "mensaje": "El token expiro" }
```

Cualquier otro error (400, 404, 422...) lo responde el propio servicio, no el gateway.

## Cómo levantarlo

```bash
cd docker && docker compose up -d     # base de datos y RabbitMQ
# levanta ms-auth y los servicios que vayas a usar
cd gateway && ./mvnw spring-boot:run  # queda en http://localhost:8080
```

## Probarlo con curl

**1. Crear un usuario** (solo la primera vez):

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"despachador1","password":"clave12345","email":"despachador1@transcargo.pe","rol":"DESPACHADOR"}'
```

**2. Iniciar sesión y obtener el token:**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"despachador1","password":"clave12345"}'
```

Te devuelve algo como `{"token":"eyJhbGciOi...","tipo":"Bearer",...}`. Copia el valor de `token`.

**3. Llamar sin token** (debe responder `401`):

```bash
curl -i http://localhost:8080/api/viajes
```

**4. Llamar con el token:**

```bash
TOKEN="pega-aqui-tu-token"

curl http://localhost:8080/api/viajes -H "Authorization: Bearer $TOKEN"
curl http://localhost:8080/vehiculos -H "Authorization: Bearer $TOKEN"
curl http://localhost:8080/api/mantenimiento/odometros/1 -H "Authorization: Bearer $TOKEN"
```
