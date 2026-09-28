\# TRANS-CARGO ERP



Sistema Integrado de Gestion y Despacho de Transporte Pesado.



Proyecto final del curso PROG5401 Desarrollo de Servicios Web II - Cibertec.



\## Descripcion



Plataforma de microservicios para una empresa de transporte interprovincial de carga pesada. Valida la documentacion de unidades y conductores antes de cada despacho, controla el peso de la carga y registra el mantenimiento por kilometraje.



\## Tecnologias



\- Java 21

\- Spring Boot 3.3.5

\- Spring Security con JWT y BCrypt

\- PostgreSQL 16

\- RabbitMQ

\- Docker



\## Microservicios



| Servicio | Puerto | Base de datos |

|---|---|---|

| gateway | 8080 | - |

| ms-viajes | 8081 | db\_viajes |

| ms-flota | 8082 | db\_flota |

| ms-conductores | 8083 | db\_conductores |

| ms-carga | 8084 | db\_carga |

| ms-mantenimiento | 8085 | db\_mantenimiento |

| ms-auth | 8086 | db\_auth |



\## Infraestructura local



```bash

cd docker

docker compose up -d

```



\## Equipo



Curso dictado por el Mg. Yuri Renzo Zambrano Macedo.

