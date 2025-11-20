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
