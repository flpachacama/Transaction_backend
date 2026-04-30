# Transaction Backend - Plataforma de Transacciones Bancarias

## 📋 Descripción del Proyecto

**Transaction Backend** es una solución de microservicios profesional construida con **Spring Boot 3.4** diseñada para la gestión integral de transacciones bancarias. Implementa una arquitectura moderna y escalable que separa responsabilidades entre dos microservicios independientes pero integrados:

- **customer-service**: Gestión de clientes y autenticación JWT
- **account-service**: Gestión de cuentas, movimientos y reportes

La solución demuestra buenas prácticas empresariales incluyendo Clean Architecture, comunicación asíncrona con RabbitMQ, persistencia con PostgreSQL, contenedorización Docker y seguridad basada en JWT.

---

## 🏗️ Arquitectura

### Visión General

```
┌─────────────────────────────────────────────────────────────┐
│                     Monorepo Maven                          │
├──────────────────────┬──────────────────────────────────────┤
│  customer-service    │       account-service                │
│  (Puerto 8081)       │       (Puerto 8082)                  │
├──────────────────────┼──────────────────────────────────────┤
│ - Clientes           │ - Cuentas                            │
│ - Autenticación JWT  │ - Movimientos                        │
│ - Publicador eventos │ - Reportes                           │
│                      │ - Consumidor de eventos RabbitMQ     │
└──────────────────────┴──────────────────────────────────────┘
          │                         │
          └──────────┬──────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
    ┌───▼────┐             ┌──────▼──┐
    │RabbitMQ│             │PostgreSQL
    │        │             │(2 bases)
    │Eventos │             │
    └────────┘             └─────────┘
```

### Patrón de Comunicación

1. **Servicio Sincrónico**: Customer Service valida clientes vía REST → Account Service consulta cuentas
2. **Servicio Asincrónico**: Customer Service publica evento `customer.created` → Account Service lo consume y registra referencia local

### Decisiones Arquitectónicas

- **Herencia JPA (JOINED)**: Person → Customer para extensibilidad
- **Relación 1:N**: Account → Movement con cascada
- **DTOs**: Aislamiento entre capas de presentación y persistencia
- **MapStruct**: Mapeo limpio sin código boilerplate
- **Global Exception Handler**: Manejo centralizado de errores con JSON estructurado
- **Spring Security**: Stateless + JWT, sin sesiones

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| **Java** | Java | 17 |
| **Framework** | Spring Boot | 3.4.6 |
| **Persistencia** | JPA/Hibernate | Spring Data JPA |
| **Base de Datos** | PostgreSQL | 16 Alpine |
| **Migraciones** | Flyway | Latest |
| **Mapeo** | MapStruct | 1.6.3 |
| **Seguridad** | Spring Security + JJWT | 0.12.6 |
| **Mensajería** | RabbitMQ | 3-Management Alpine |
| **API Docs** | Springdoc OpenAPI | 2.6.0 |
| **Testing** | JUnit 5 + Mockito | Latest |
| **Build** | Maven | 3.9+ |
| **Containerización** | Docker + Docker Compose | Latest |
| **Logs** | SLF4J/Logback | Incluido en Spring |

---

## 📁 Estructura del Proyecto

```
Transaction_backend/
├── pom.xml (Padre Maven - agregador de módulos)
├── docker-compose.yml (Orquestación de servicios)
├── .gitignore
├── README.md (Este archivo)
│
├── customer-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/transaction/customer/
│       ├── config/          # Configuración (JWT, RabbitMQ, OpenAPI)
│       ├── controller/      # REST Controllers
│       ├── dto/            # Data Transfer Objects
│       ├── entity/         # Entidades JPA (Person, Customer)
│       ├── exception/      # Excepciones y manejo global
│       ├── mapper/         # Mapstruct mappers
│       ├── messaging/      # Publicadores de eventos
│       ├── repository/     # Data Access Layer
│       ├── security/       # JWT y configuración de seguridad
│       ├── service/        # Lógica de negocio (interfaz + impl)
│       ├── util/          # Constantes y utilidades
│       └── CustomerServiceApplication.java
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/   # Scripts SQL Flyway
│   └── src/test/java/      # Tests unitarios
│
├── account-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/transaction/account/
│       ├── config/         # RabbitMQ, OpenAPI
│       ├── controller/     # REST Controllers
│       ├── dto/           # DTOs
│       ├── entity/        # Entidades (Account, Movement, CustomerInfo)
│       ├── exception/     # Manejo de errores
│       ├── mapper/        # Mapstruct
│       ├── messaging/     # Consumidor RabbitMQ
│       ├── repository/    # Data Access
│       ├── service/       # Lógica (Account, Movement)
│       ├── util/         # Constantes
│       └── AccountServiceApplication.java
│   └── src/main/resources/
│       ├── application.yml
│       └── db/migration/  # Scripts SQL Flyway
│   └── src/test/java/     # Tests unitarios
```

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos Previos

- Docker Desktop instalado (incluye Docker Engine y Docker Compose)
- Git
- Terminal/Consola (cmd.exe en Windows)

### Pasos de Ejecución

#### 1. Clonar o navegar al repositorio

```bash
cd "c:\Users\fredd\Desktop\Freddy Leonel\Transaction_backend"
```

#### 2. Levantar la infraestructura y aplicaciones

```bash
docker compose up --build
```

Este comando:
- Construye las imágenes Docker de ambos servicios
- Crea y levanta contenedores para PostgreSQL (2 bases), RabbitMQ, PgAdmin
- Inicia ambos microservicios
- Ejecuta migraciones Flyway automáticamente
- Carga datos iniciales (seed)

#### 3. Verificar que los servicios están listos

Espera hasta ver logs como:
```
customer-service | [...] Started CustomerServiceApplication in X.XXX seconds
account-service  | [...] Started AccountServiceApplication in X.XXX seconds
```

#### 4. Detener los servicios

```bash
docker compose down
```

Para limpiar volúmenes también:
```bash
docker compose down -v
```

---

## 🌐 URLs Importantes

### Swagger UI (Documentación Interactiva)

| Servicio | URL |
|----------|-----|
| **Customer Service** | http://localhost:8081/swagger-ui.html |
| **Account Service** | http://localhost:8082/swagger-ui.html |

### Interfaces de Administración

| Herramienta | URL | Credenciales |
|-------------|-----|--------------|
| **RabbitMQ Management** | http://localhost:15672 | guest / guest |
| **PgAdmin** | http://localhost:5050 | admin@transaction.example.com / admin123 |

### Bases de Datos

| Base | Host | Puerto | Usuario | Password |
|------|------|--------|---------|----------|
| **customer_db** | localhost | 5433 | postgres | postgres |
| **account_db** | localhost | 5434 | postgres | postgres |

---

## 🔐 Credenciales de Prueba

### Cliente para Login

```
clientId: joselema
password: 1234
```

Otros clientes disponibles (seed):
- `mari` / `5678`
- `juanito` / `1245`

### Cuentas Iniciales

- **478758** (Ahorro) - Saldo: 1425 (después de retiro de 575)
- **225487** (Corriente) - Saldo: 700 (100 inicial + 600 depósito)
- **495878** (Ahorros) - Saldo: 150
- **496825** (Ahorros) - Saldo: 0 (540 inicial - 540 retiro)

---

## 📡 Endpoints Principales

### Customer Service

#### Autenticación
```http
POST /api/auth/login
Content-Type: application/json

{
  "clientId": "joselema",
  "password": "1234"
}

Response: 
{
  "token": "eyJhbGc...",
  "type": "Bearer"
}
```

#### Gestión de Clientes
```http
# Crear cliente
POST /api/clientes
Content-Type: application/json

# Listar clientes
GET /api/clientes

# Obtener cliente
GET /api/clientes/{id}

# Actualizar cliente
PUT /api/clientes/{id}

# Eliminar cliente
DELETE /api/clientes/{id}
```

### Account Service

#### Gestión de Cuentas
```http
# Crear cuenta
POST /api/cuentas

# Listar cuentas
GET /api/cuentas

# Obtener cuenta
GET /api/cuentas/{id}

# Actualizar cuenta
PUT /api/cuentas/{id}

# Eliminar cuenta
DELETE /api/cuentas/{id}
```

#### Gestión de Movimientos
```http
# Registrar movimiento (depósito/retiro)
POST /api/movimientos
{
  "accountNumber": "478758",
  "movementType": "WITHDRAW",  # DEPOSIT o WITHDRAW
  "amount": 575.00
}

# Listar movimientos
GET /api/movimientos

# Obtener movimiento
GET /api/movimientos/{id}

# Eliminar movimiento
DELETE /api/movimientos/{id}
```

#### Reportes
```http
# Estado de cuenta por rango de fechas
GET /api/reportes?startDate=2022-02-01&endDate=2022-02-10&clientId=joselema

Response:
[
  {
    "date": "2022-02-01",
    "client": "Jose Lema",
    "accountNumber": "478758",
    "type": "AHORRO",
    "initialBalance": 2000,
    "status": true,
    "movement": -575,
    "availableBalance": 1425
  }
]
```

---

## 🧪 Pruebas Unitarias

### Tests Implementados

#### customer-service
**Archivo**: `src/test/java/com/transaction/customer/service/impl/CustomerServiceImplTest.java`

**Caso**: `createCustomer_success()`
- Valida que se guarde correctamente un cliente
- Verifica invocación al repository
- Comprueba que se retorna DTO correcto
- Valida publicación de evento RabbitMQ

```bash
mvn -pl customer-service test
```

#### account-service
**Archivo**: `src/test/java/com/transaction/account/service/impl/MovementServiceImplTest.java`

**Casos**:
1. `withdraw_insufficientBalance_shouldThrowException()` - Retiro superior al saldo lanza excepción "Saldo no disponible"
2. `withdraw_sufficientBalance_shouldSuccess()` - Retiro exitoso actualiza balance

```bash
mvn -pl account-service test
```

### Ejecutar Todos los Tests

```bash
mvn clean test
```

### Patrón Usado: AAA (Arrange-Act-Assert)

```java
@Test
void testCase() {
    // Arrange: Preparar datos y mocks
    Account account = new Account();
    
    // Act: Ejecutar la acción
    movementService.createMovement(request);
    
    // Assert: Verificar resultados
    assertThat(account.getCurrentBalance()).isEqualTo(expectedValue);
}
```

---

## 🔄 Flujo de Eventos RabbitMQ

### Evento: customer.created

**Publicador**: customer-service (cuando se crea un cliente)

**Consumidor**: account-service (registra referencia de cliente)

**Estructura del Evento**:
```json
{
  "clientId": "joselema",
  "name": "Jose Lema",
  "status": true
}
```

**Exchange**: `customer.exchange`

**Queue**: `customer.created.queue`

**Routing Key**: `customer.created`

---

## 🔒 Seguridad

### JWT (JSON Web Token)

- **Generación**: Al hacer login en `/api/auth/login`
- **Duración**: 60 minutos (configurable)
- **Algoritmo**: HS256 (HMAC SHA-256)
- **Claim adicional**: Incluye nombre y estatus del cliente

### Endpoints Públicos

```
/api/auth/**
/swagger-ui/**
/v3/api-docs/**
/webjars/**
```

### Endpoints Protegidos

Todos los demás requieren header:
```
Authorization: Bearer <token>
```

### Encoding de Contraseña

- Algoritmo: BCrypt (DelegatingPasswordEncoder)
- Entrada: Contraseña en texto plano
- Salida: Hash seguro almacenado en BD

---

## 🐛 Validaciones y Manejo de Errores

### Validaciones en Entrada

- `@NotBlank`: Campos obligatorios de texto
- `@NotNull`: Campos obligatorios
- `@Positive`: Montos positivos
- `@PositiveOrZero`: Saldo inicial no negativo

### Excepciones Personalizadas

| Excepción | HTTP | Mensaje |
|-----------|------|---------|
| `ResourceNotFoundException` | 404 | "Recurso no encontrado" |
| `InsufficientBalanceException` | 400 | "Saldo no disponible" |
| `BusinessException` | 400 | Mensaje específico del negocio |
| `MethodArgumentNotValidException` | 400 | Detalles de validación |

### Respuesta de Error Estándar

```json
{
  "timestamp": "2026-04-29T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "path": "/api/movimientos",
  "details": []
}
```

**Versión**: 1.1.0  
**Última actualización**: 29 de abril de 2026  
**Estado**: ✅ Completo y Listo para Evaluación  
**Autor**: Freddy Leonel Pachacama
