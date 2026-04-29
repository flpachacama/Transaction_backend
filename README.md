# Transaction_backend

Monorepo profesional construido con Maven multi-module para una prueba técnica SemiSenior basada en microservicios con Spring Boot.

## Descripción del proyecto

`Transaction_backend` es una base empresarial para una solución de transacciones financieras separada en dos microservicios independientes:

- `customer-service`: gestión de persona, cliente, CRUD de clientes, login JWT y publicación de eventos por RabbitMQ.
- `account-service`: gestión de cuenta, movimiento, CRUD de cuentas, CRUD de movimientos, reportes y consumo de eventos por RabbitMQ.

Esta fase deja preparada la estructura base, la infraestructura local y la configuración inicial para que cada servicio evolucione de forma aislada siguiendo buenas prácticas de Clean Architecture.

## Arquitectura

```text
Transaction_backend/
├── pom.xml
├── docker-compose.yml
├── README.md
├── .gitignore
├── customer-service/
│   ├── pom.xml
│   └── src/main/java/com/transaction/customer/
└── account-service/
	├── pom.xml
	└── src/main/java/com/transaction/account/
```

### Organización por módulos

- `customer-service`: microservicio orientado a cliente/persona.
- `account-service`: microservicio orientado a cuentas/movimientos/reportes.

Cada módulo contiene su propio `pom.xml`, su clase principal Spring Boot y su archivo `src/main/resources/application.yml` inicial.

## Stack tecnológico

- Java 17
- Spring Boot 3.4.6
- Maven multi-module
- PostgreSQL
- RabbitMQ
- Flyway
- Lombok
- MapStruct
- SLF4J / logging por defecto de Spring Boot
- Swagger OpenAPI con `springdoc-openapi`
- Docker Compose
- JWT Security: planificado para una fase posterior

## Puertos utilizados

### `customer-service`
- Aplicación: `8081`
- PostgreSQL: `5433` en host → `5432` en contenedor

### `account-service`
- Aplicación: `8082`
- PostgreSQL: `5434` en host → `5432` en contenedor

### Infraestructura compartida
- RabbitMQ AMQP: `5672`
- RabbitMQ Management UI: `15672`
- pgAdmin: `5050`

## Cómo ejecutar la infraestructura

Levantar únicamente la infraestructura local:

```bash
docker compose up -d
```

Detener y eliminar los contenedores:

```bash
docker compose down
```

## Cómo compilar el monorepo

Compilación desde la raíz del proyecto:

```bash
mvn clean install
```

Compilación por módulo:

```bash
mvn -pl customer-service clean package
mvn -pl account-service clean package
```

## Accesos rápidos

- RabbitMQ Management UI: http://localhost:15672
- pgAdmin: http://localhost:5050

Credenciales de infraestructura local:

- PostgreSQL: `postgres / postgres`
- RabbitMQ: `guest / guest`
- pgAdmin: `admin@transaction.local / admin123`

## Estado de la fase 1

En esta fase solo se crea la base del monorepo y la infraestructura externa. Las capas de dominio, aplicación, persistencia, seguridad JWT y contratos de API se implementarán en etapas posteriores.

## customer-service implementado en esta entrega

### Endpoints principales

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`
- `POST /api/auth/login`

### RabbitMQ

- Exchange: `customer.exchange`
- Routing key: `customer.created`

### Seed inicial

Al iniciar el servicio con Flyway se crean y cargan automáticamente 3 clientes iniciales del ejercicio técnico.
