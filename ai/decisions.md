# Decisiones de diseño tomadas con ayuda de IA

## 1. Estructura de paquetes y carpetas inicial

**Decisión:**  
Utilizar una arquitectura hexagonal con capas: `api`, `application`, `domain`, `infrastructure`, `shared`, y separar la documentación y contrato en `ai/`, `docs/`, `openapi/`.

**Motivo:**  
La IA (Cursor) propuso y generó la estructura inicial basada en Clean Architecture. Yo validé la propuesta y la acepté porque:
- Permite separar claramente dominio de infraestructura.
- Facilita añadir adaptadores SOAP, REST y persistencia sin acoplar el dominio.
- Hace más fácil extender a otros canales (por ejemplo, otro API REST o mensajería).

**Impacto:**  
El resto de la prueba técnica se implementará siguiendo esta separación de responsabilidades, lo que facilita mantenibilidad y testeo.
