# Registro de prompts usados con IA

## 1. Creación de estructura base del proyecto

**Fecha:** 2025-11-19  
**Objetivo:** Crear la estructura de paquetes hexagonal (api, application, domain, infrastructure, shared), las carpetas de documentación (`ai`, `docs`, `openapi`) y el archivo `.gitignore` para un proyecto Spring Boot 3.

**Contexto técnico:**
- Proyecto: Spring Boot 3, Maven, Java 17.
- Package base: `com.hiberus.payment_initiation`.

**Prompt enviado a la IA (Cursor, resumido):**

> "Actúa como un desarrollador senior Java/Spring Boot especializado en Clean Architecture y SOLID.  
> Ya tengo un proyecto Spring Boot con package base `com.hiberus.payment_initiation`.  
> Crea los paquetes api, application, domain, infrastructure (config, persistence, soap), shared; crea las carpetas `ai`, `docs`, `openapi` con archivos base, y genera un `.gitignore` adecuado para Java/Maven/IDEs..."

**Resultado:**
- Se crearon los paquetes:
  - `com.hiberus.payment_initiation.api`
  - `com.hiberus.payment_initiation.application`
  - `com.hiberus.payment_initiation.domain`
  - `com.hiberus.payment_initiation.infrastructure.config`
  - `com.hiberus.payment_initiation.infrastructure.persistence`
  - `com.hiberus.payment_initiation.infrastructure.soap`
  - `com.hiberus.payment_initiation.shared`
- Se añadieron placeholders en cada paquete.
- Se crearon las carpetas:
  - `ai/decisions.md`, `ai/prompts.md`, `ai/generations/`
  - `docs/ARQUITECTURA.md`
  - `openapi/payment-initiation.yaml`
- Se creó/actualizó `.gitignore` para ignorar `target/`, `.idea/`, `.vscode/`, `node_modules/`, archivos de logs, etc.

## 2. Definición del contrato OpenAPI para PaymentOrder

**Fecha:** 2025-11-19  
**Objetivo:** Definir el contrato REST (OpenAPI 3.0) para el microservicio de Payment Initiation, mapeando el WSDL `PaymentOrderService.wsdl` y la colección de Postman a recursos REST.

**Uso de IA:**
- Utilicé ChatGPT como apoyo de diseño para:
  - Proponer los endpoints REST `/payment-initiation/payment-orders`, `/payment-initiation/payment-orders/{paymentOrderId}` y `/payment-initiation/payment-orders/{paymentOrderId}/status`.
  - Diseñar los esquemas JSON `PaymentOrderInitiationRequest`, `PaymentOrderResource`, `PaymentOrderStatusResource`, `AccountReference`, `Amount` y `ErrorResponse`.
  - Alinear los campos con el servicio SOAP legacy (`externalId`, `debtorIban`, `creditorIban`, `amount`, `currency`, `remittanceInfo`, `requestedExecutionDate`, `paymentOrderId`, `status`, `lastUpdate`).

**Resultado:**
- Se actualizó `openapi/payment-initiation.yaml` con un contrato OpenAPI 3.0 completo, que será la fuente de verdad para generar controladores y DTOs.

## 3. Generación de DTOs, casos de uso y controlador

**Fecha:** 2025-11-19  
**Objetivo:** A partir del contrato `openapi/payment-initiation.yaml`, generar la capa API (DTOs + controlador REST), las interfaces de casos de uso en `application` y el modelo de dominio mínimo en `domain`, siguiendo Clean Architecture y principios SOLID.

**Uso de IA (Cursor):**  
Le pedí a la IA que, actuando como desarrollador senior Java/Spring Boot en banca, realizara las siguientes tareas:

- Leer el contrato OpenAPI `openapi/payment-initiation.yaml`.
- Crear en `com.hiberus.payment_initiation.api` los DTOs:
  - `PaymentOrderInitiationRequestDto`
  - `PaymentOrderResourceDto`
  - `PaymentOrderStatusResourceDto`
  - `AccountReferenceDto`
  - `AmountDto`
  - `ErrorResponseDto`  
  usando tipos Java adecuados (`LocalDate`, `OffsetDateTime`, etc.) y anotaciones de Bean Validation en los campos requeridos.

- Definir en `com.hiberus.payment_initiation.domain` el modelo de dominio puro:
  - Entidad `PaymentOrder`
  - Value objects `Account` y `Money`
  - Enum `PaymentOrderStatus`  
  **sin anotaciones de frameworks** (sin JPA, sin Bean Validation, sin Jackson) para mantener el dominio desacoplado y limpio.

- Definir en `com.hiberus.payment_initiation.application` los casos de uso:
  - `InitiatePaymentOrderUseCase`
  - `RetrievePaymentOrderUseCase`
  - `RetrievePaymentOrderStatusUseCase`
  - Comando `PaymentOrderInitiationCommand` como input de dominio para la iniciación de la orden.

- Crear un mapper en la capa API:
  - Clase `PaymentOrderMapper` para convertir entre:
    - DTOs ↔ objetos de dominio
    - `PaymentOrderInitiationRequestDto` → `PaymentOrderInitiationCommand`
    - `PaymentOrder` → `PaymentOrderResourceDto`
    - Estado → `PaymentOrderStatusResourceDto`.

- Crear el controlador REST `PaymentOrderController` en `api` con los endpoints:
  - `POST /payment-initiation/payment-

## 4. Implementación de casos de uso, puerto legacy y stub SOAP

**Fecha:** 2025-11-20  
**Objetivo:** Completar el flujo de negocio desde el controlador REST hasta una integración simulada con el servicio SOAP legacy, siguiendo arquitectura hexagonal:
API → Application (use cases) → Domain SPI (puerto) → Infrastructure (adapter SOAP stub).

**Uso de IA (Cursor):**  
Le pedí a la IA, actuando como desarrollador senior Java/Spring Boot en banca con Clean Architecture y SOLID, que realizara lo siguiente:

1. **Definir el puerto hacia el sistema legacy (servicio SOAP):**
   - Crear el paquete:
     - `com.hiberus.payment_initiation.domain.spi`
   - Crear la interfaz:
     - `PaymentOrderLegacyClient`
   - Métodos:
     - `PaymentOrder initiatePaymentOrder(PaymentOrder paymentOrder);`
     - `Optional<PaymentOrder> findById(String paymentOrderId);`
     - `Optional<PaymentOrderStatus> findStatus(String paymentOrderId);`
   - Rol: actuar como **abstracción** del servicio SOAP `PaymentOrderService`, desacoplando la lógica de negocio de la tecnología SOAP concreta.

2. **Implementar los casos de uso en la capa application:**
   - Crear el paquete:
     - `com.hiberus.payment_initiation.application.impl`
   - Implementar:
     - `InitiatePaymentOrderService implements InitiatePaymentOrderUseCase`
     - `RetrievePaymentOrderService implements RetrievePaymentOrderUseCase`
     - `RetrievePaymentOrderStatusService implements RetrievePaymentOrderStatusUseCase`
   - Inyectar `PaymentOrderLegacyClient` por constructor (Dependency Inversion).
   - Lógica principal:
     - `initiate(PaymentOrderInitiationCommand command)`:
       - Construye un `PaymentOrder` de dominio a partir del comando.
       - Asigna un estado inicial (`PaymentOrderStatus.INITIATED`).
       - Delegar en `legacyClient.initiatePaymentOrder(paymentOrder)`.
       - Devolver la entidad resultante.
     - `retrieveById(String paymentOrderId)`:
       - Delegar en `legacyClient.findById(paymentOrderId)`.
       - Si no existe, lanzar `PaymentOrderNotFoundException`.
     - `retrieveStatus(String paymentOrderId)`:
       - Delegar en `legacyClient.findStatus(paymentOrderId)`.
       - Si no existe, lanzar `PaymentOrderNotFoundException`.

3. **Gestión de “no encontrado”:**
   - Crear la excepción:
     - `com.hiberus.payment_initiation.shared.exception.PaymentOrderNotFoundException`
   - Extiende `RuntimeException` y construye un mensaje claro a partir del `paymentOrderId`.
   - Esta excepción será mapeada más adelante a un `ErrorResponseDto` mediante `@ControllerAdvice`.

4. **Crear la implementación stub del puerto SOAP en infraestructura:**
   - En el paquete:
     - `com.hiberus.payment_initiation.infrastructure.soap`
   - Crear la clase:
     - `LegacyPaymentOrderSoapClient implements PaymentOrderLegacyClient`
   - Anotación: `@Component`.
   - Comportamiento simulado:
     - `initiatePaymentOrder(...)`:
       - Genera un ID (por ejemplo con `UUID.randomUUID()` si no existe).
       - Ajusta estado a `PENDING_EXECUTION`.
       - Rellena `creationDateTime` y `lastUpdateDateTime` con `OffsetDateTime.now()`.
       - Añade comentarios `// TODO` donde iría la llamada real al SOAP usando el WSDL.
     - `findById(...)`:
       - Devuelve un `Optional<PaymentOrder>` simulado.
       - En algunos casos puede devolver `Optional.empty()` para probar el escenario “no encontrado”.
     - `findStatus(...)`:
       - Devuelve un `Optional<PaymentOrderStatus>` simulado (por ejemplo siempre `EXECUTED`), también con comentarios `// TODO`.

**Resultado:**
- El flujo ahora está completamente cableado:
  - `PaymentOrderController` (capa **api**) recibe/valida la petición, la mapea y delega en los casos de uso.
  - Las implementaciones de los casos de uso en **application.impl** orquestan la lógica y dependen de la abstracción `PaymentOrderLegacyClient`.
  - La interfaz `PaymentOrderLegacyClient` en **domain.spi** actúa como puerto hacia el sistema legacy.
  - `LegacyPaymentOrderSoapClient` en **infrastructure.soap** es el adapter concreto que implementa el puerto, actualmente con lógica stub que simula el servicio SOAP real.

- Se mantiene la arquitectura hexagonal:
  - La capa de dominio y aplicación **no dependen** de detalles SOAP.
  - La integración SOAP puede ser sustituida en el futuro por una implementación real sin impactar la API ni los casos de uso.
- El proyecto sigue compilando correctamente y el contexto de Spring arranca con los beans configurados (`@Service`, `@Component`).

## 5. Manejo global de errores con GlobalExceptionHandler

**Objetivo:** Estandarizar las respuestas de error de la API usando `ErrorResponseDto` y `@RestControllerAdvice`, mapeando:
- `PaymentOrderNotFoundException` → HTTP 404, code `PAYMENT_ORDER_NOT_FOUND`.
- `MethodArgumentNotValidException` → HTTP 400, code `VALIDATION_ERROR`, con detalle de campos inválidos.
- `Exception` genérica → HTTP 500, code `INTERNAL_ERROR`.

## 6. Generación de tests de casos de uso y controlador

**Fecha:** 2025-11-20  
**Objetivo:** Crear una suite básica de tests automatizados que valide:
- La lógica de los casos de uso en `application.impl` (sin levantar Spring).
- El comportamiento del controlador `PaymentOrderController` y del `GlobalExceptionHandler` usando MockMvc.

**Uso de IA (Cursor):**  
Le pedí a la IA, actuando como desarrollador senior enfocado en calidad, que:

- Generara tests unitarios puros (JUnit 5 + Mockito) para:
  - `InitiatePaymentOrderServiceTest`
  - `RetrievePaymentOrderServiceTest`
  - `RetrievePaymentOrderStatusServiceTest`

  Con casos como:
  - Iniciación de una orden de pago llamando al mock `PaymentOrderLegacyClient` y verificando que:
    - Se invoca el puerto.
    - Se devuelve un `PaymentOrder` con datos esperados.
  - Recuperación de una orden existente (`findById` devuelve `Optional.of(...)`).
  - Lanzamiento de `PaymentOrderNotFoundException` cuando `findById` o `findStatus` devuelven `Optional.empty()`.

- Generara tests de controlador con MockMvc:
  - `PaymentOrderControllerTest` usando `@WebMvcTest` y `@MockBean` para los casos de uso.
  - Casos de prueba:
    - `POST /payment-initiation/payment-orders` devuelve **201** y un JSON con `paymentOrderId` y `status`.
    - Petición inválida (sin campos requeridos) devuelve **400** con `code = "VALIDATION_ERROR"` y lista de `details`.
    - Cuando el caso de uso lanza `PaymentOrderNotFoundException`, el endpoint `GET /payment-initiation/payment-orders/{id}` devuelve **404** con `code = "PAYMENT_ORDER_NOT_FOUND"`.
    - `GET /payment-initiation/payment-orders/{id}/status` devuelve **200** y `status = "EXECUTED"` (escenario feliz).

**Resultado:**
- Se crearon las clases de test bajo `src/test/java/com/hiberus/payment_initiation`:
  - `application.impl.InitiatePaymentOrderServiceTest`
  - `application.impl.RetrievePaymentOrderServiceTest`
  - `application.impl.RetrievePaymentOrderStatusServiceTest`
  - `api.PaymentOrderControllerTest`
- `mvn test` pasa correctamente y los tests validan tanto la lógica de los casos de uso como el contrato HTTP (códigos de estado y formato de respuesta).
