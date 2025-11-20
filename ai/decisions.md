# Decisiones de diseño tomadas con ayuda de IA

## 1. Arquitectura hexagonal y estructura de paquetes

**Decisión:**  
Utilizar una arquitectura hexagonal (Clean Architecture) separando claramente:
- `api` (controllers + DTOs)
- `application` (casos de uso)
- `domain` (modelo de negocio y puertos)
- `infrastructure` (adapters, SOAP, persistencia)
- `shared` (excepciones y utilidades comunes).

**Cómo ayudó la IA:**  
Con apoyo de la IA definí la estructura de paquetes de forma explícita y generé los placeholders iniciales. La IA propuso la separación y yo la validé porque se adapta bien a un contexto bancario, facilita pruebas y evita acoplar el dominio a frameworks.

---

## 2. Contrato REST OpenAPI como fachada sobre SOAP legacy

**Decisión:**  
Diseñar un contrato REST (`openapi/payment-initiation.yaml`) que actúa como fachada moderna sobre el servicio SOAP `PaymentOrderService`, con endpoints:

- `POST /payment-initiation/payment-orders`
- `GET /payment-initiation/payment-orders/{paymentOrderId}`
- `GET /payment-initiation/payment-orders/{paymentOrderId}/status`

y recursos como `PaymentOrderInitiationRequest`, `PaymentOrderResource`, `PaymentOrderStatusResource`.

**Cómo ayudó la IA:**  
La IA colaboró en proponer el diseño de los recursos y el mapeo de campos desde el WSDL (por ejemplo `externalId` → `externalReference`, IBAN deudor/acreedor, amount + currency, etc.), siguiendo buenas prácticas de diseño de APIs REST en banca.

---

## 3. Dominio limpio sin anotaciones vs DTOs con validación

**Decisión:**  
Mantener el **dominio completamente libre de anotaciones** de frameworks (sin JPA, sin Bean Validation, sin Jackson) y concentrar las anotaciones de validación únicamente en los DTOs de la capa `api`.

**Cómo ayudó la IA:**  
La IA reforzó la idea de que el dominio debe ser puro y probando con ejemplos, confirmamos que:
- Las reglas de entrada se validan en los DTOs con `@Valid`, `@NotNull`, etc.
- El dominio trabaja solo con objetos ya válidos, lo que mejora mantenibilidad y testeo.
Esto alinea el diseño con los principios de Clean Architecture y SOLID.

---

## 4. Puerto hacia el sistema legacy y adapter SOAP stub

**Decisión:**  
Introducir un puerto de dominio `PaymentOrderLegacyClient` en `domain.spi` y una implementación `LegacyPaymentOrderSoapClient` en `infrastructure.soap`, que por ahora funciona como **stub** (simula el SOAP real) pero deja clara la extensión futura.

**Cómo ayudó la IA:**  
La IA ayudó a diseñar las firmas del puerto (`initiatePaymentOrder`, `findById`, `findStatus`) y la implementación stub que:
- Genera IDs y estados simulados.
- Rellena timestamps.
- Deja `// TODO` donde iría la llamada SOAP real.  
Esto muestra una integración desacoplada: los casos de uso dependen de la abstracción y no del detalle SOAP.

## 5. Estrategia de manejo de errores en la API

**Decisión:**  
Centralizar el manejo de errores en un `@RestControllerAdvice` (`GlobalExceptionHandler`) y estandarizar todas las respuestas de error mediante `ErrorResponseDto`.

**Detalle de la estrategia:**
- Para `PaymentOrderNotFoundException`:
  - HTTP status: **404 NOT_FOUND**
  - `code`: `PAYMENT_ORDER_NOT_FOUND`
  - `message`: mensaje de la excepción (incluye el `paymentOrderId`).
  - Uso principal en los casos de uso `RetrievePaymentOrderUseCase` y `RetrievePaymentOrderStatusUseCase`.

- Para errores de validación de entrada (`MethodArgumentNotValidException` por @Valid en los DTOs):
  - HTTP status: **400 BAD_REQUEST**
  - `code`: `VALIDATION_ERROR`
  - `message`: texto genérico ("Request validation failed").
  - `details`: lista con los errores campo a campo en formato `"field: message"`.

- Para cualquier otra excepción (`Exception`):
  - HTTP status: **500 INTERNAL_SERVER_ERROR**
  - `code`: `INTERNAL_ERROR`
  - `message`: mensaje genérico que no expone detalles internos.
  - Log a nivel ERROR con la traza para diagnóstico interno.

**Cómo ayudó la IA:**  
Con ayuda de la IA se definieron:
- La estructura del `GlobalExceptionHandler`.
- La forma de mapear las distintas excepciones a `ErrorResponseDto`.
- Buenas prácticas bancarias: no filtrar detalles internos al consumidor, pero sí registrar información suficiente en logs (WARN para errores funcionales, ERROR para fallos inesperados).

**Impacto:**  
- La API ofrece respuestas de error coherentes y fáciles de consumir por clientes y herramientas (incluida la colección de Postman).
- El código de los controladores se mantiene limpio, delegando el manejo de excepciones en una capa transversal.
- Se refuerza la separación de responsabilidades y se mejora la observabilidad y mantenibilidad del servicio.

## 6. Estrategia de testing (use cases + API)

**Decisión:**  
Adoptar una estrategia de pruebas en dos niveles:

1. **Tests unitarios de casos de uso (application)**  
   - Se testean las clases de `application.impl` aisladas del framework:
     - `InitiatePaymentOrderService`
     - `RetrievePaymentOrderService`
     - `RetrievePaymentOrderStatusService`
   - Se usan **mocks** de `PaymentOrderLegacyClient` (Mockito) para:
     - Verificar que los casos de uso delegan correctamente en el puerto de dominio.
     - Validar el comportamiento cuando el puerto devuelve datos (`Optional.of`) o no devuelve nada (`Optional.empty` → `PaymentOrderNotFoundException`).
   - No se levanta el contexto de Spring (`@SpringBootTest`), lo que hace los tests más rápidos y centrados en lógica de negocio.

2. **Tests de controlador con MockMvc (API + errores)**  
   - Se usa `@WebMvcTest(PaymentOrderController.class)` para probar:
     - El mapeo de endpoints, códigos HTTP y estructura de las respuestas JSON.
     - La integración del `GlobalExceptionHandler` con los controladores.
   - Los casos de uso se mockean con `@MockBean`, verificando:
     - Escenario feliz de creación (**201** Created).
     - Errores de validación (**400** con `code="VALIDATION_ERROR"`).
     - Recursos no encontrados (**404** con `code="PAYMENT_ORDER_NOT_FOUND"`).

**Cómo ayudó la IA:**  
La IA ayudó a:
- Proponer la separación clara entre tests unitarios puros (sin Spring) y tests web con MockMvc.
- Definir los casos de prueba clave que aportan mayor valor:
  - Flujo feliz de iniciación de orden.
  - Escenario “no encontrado”.
  - Manejo de errores de validación.
- Generar la plantilla de clases de test con las anotaciones correctas de JUnit 5, Mockito y Spring Test.

**Impacto:**  
- Se valida el comportamiento de negocio sin acoplar los tests a detalles de infraestructura.
- Se asegura que la API cumple el contrato HTTP esperado (códigos y formato de error).
- Facilita refactorizar implementación interna (por ejemplo, cambiar el adapter SOAP) sin romper los tests siempre que se mantenga el contrato y la lógica de los casos de uso.
