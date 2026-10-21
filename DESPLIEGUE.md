# Despliegue con Docker

Todo el sistema (MySQL, RabbitMQ y los 7 servicios) se levanta con el `docker-compose.yml` de la raíz.
Solo el gateway queda accesible desde fuera, en el puerto **8080**. El resto se comunica por la red interna de Docker usando el nombre de cada servicio.

> El archivo `docker/docker-compose.yml` sigue siendo el de desarrollo: solo levanta MySQL y RabbitMQ para ejecutar los servicios desde el IDE.

## Requisitos

- Docker con el plugin Compose v2 (`docker compose version`).
- Unos 3 GB de RAM libres para ejecutar todo (ver [Memoria](#memoria)).
- No hace falta Java ni Maven en la máquina: se compila dentro de Docker.

## Servicios y puertos

| Servicio | Puerto interno | Publicado | Base de datos |
|---|---|---|---|
| gateway | 8080 | **8080** | — |
| ms-viajes | 8081 | no | db_viajes |
| ms-flota | 8082 | no | db_flota |
| ms-conductores | 8083 | no | db_conductores |
| ms-carga | 8084 | no | db_carga |
| ms-mantenimiento | 8085 | no | db_mantenimiento |
| ms-auth | 8086 | no | db_auth |
| mysql | 3306 | no | — |
| rabbitmq | 5672 / 15672 | no | — |

Rutas del gateway: `/auth/**`, `/api/viajes/**`, `/vehiculos/**`, `/conductores/**`, `/api/carga/**`, `/api/mantenimiento/**`.

## Configuración

Las variables están en `.env.example`. Para cambiarlas:

```bash
cp .env.example .env
# editar .env: contraseñas y JWT_SECRET
```

Si no existe `.env`, el compose arranca con valores por defecto de desarrollo.
La clave `JWT_SECRET` se define una sola vez y la usan ms-auth (para firmar) y el gateway (para validar).

La contraseña de MySQL (`MYSQL_ROOT_PASSWORD`) solo se aplica cuando se crea el volumen por primera vez. Si la cambias después, borra el volumen (`docker compose down -v`, se pierden los datos).

## Comandos

```bash
# construir y levantar todo
docker compose up -d --build

# ver el estado
docker compose ps

# ver logs (todos, o de un servicio)
docker compose logs -f
docker compose logs -f ms-viajes

# apagar (los datos se conservan en el volumen mysqldata)
docker compose down

# apagar y borrar también los datos
docker compose down -v
```

La primera construcción descarga las dependencias de Maven y tarda varios minutos. Las siguientes reutilizan la caché mientras no cambie el `pom.xml`.

Los servicios tardan entre 20 y 60 segundos en arrancar después de que MySQL y RabbitMQ estén listos. Si el gateway responde 503 al principio, espera y vuelve a intentar.

## Despliegue en una máquina virtual Linux

1. Instalar Docker (por ejemplo en Ubuntu: `curl -fsSL https://get.docker.com | sh`) y agregar tu usuario al grupo `docker` (`sudo usermod -aG docker $USER`, luego cerrar y abrir sesión).
2. Clonar el repositorio:
   ```bash
   git clone <url-del-repositorio> trans-cargo-erp
   cd trans-cargo-erp
   ```
3. Crear el `.env` con valores reales:
   ```bash
   cp .env.example .env
   nano .env   # cambiar MYSQL_ROOT_PASSWORD, RABBITMQ_PASSWORD y JWT_SECRET
   ```
4. Levantar:
   ```bash
   docker compose up -d --build
   ```
5. Abrir el puerto **8080** en el firewall o grupo de seguridad de la nube. No abras ningún otro.
6. Probar: `curl http://<ip-de-la-vm>:8080/auth/login` debe responder (aunque sea con un error de validación).

Para actualizar después de un cambio en el repositorio:

```bash
git pull
docker compose up -d --build
```

## Memoria

Cada JVM está limitada con `JAVA_TOOL_OPTIONS` (por defecto `-Xms64m -Xmx192m -XX:MaxMetaspaceSize=128m -Xss512k -XX:+UseSerialGC`; se cambia con `JAVA_OPTS` en `.env`).

Estimación aproximada en ejecución:

| Componente | RAM aprox. |
|---|---|
| Cada servicio Spring Boot (7) | 250–330 MB |
| MySQL | 450–550 MB |
| RabbitMQ | 120–150 MB |
| **Total** | **≈ 2,3–3 GB** |

Recomendación: una VM con **4 GB de RAM** (o 2 GB más 2 GB de swap, con arranque más lento). La compilación con Maven también consume memoria; si la VM es pequeña y la construcción falla, construye los servicios de uno en uno:

```bash
for s in ms-auth ms-flota ms-conductores ms-carga ms-mantenimiento ms-viajes gateway; do docker compose build $s; done
docker compose up -d
```
