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
