# 📚 Arquitectura Software - LMS

Este repositorio contiene el backend del sistema LMS desarrollado en Java con Spring Boot y Gradle.

## 📦 Estructura del proyecto

- `src/main/java/com/lms/educa/Entidades/`: Modelos del dominio (Usuario, Materia, Evaluación, etc.)
- `src/main/java/com/lms/educa/service/`: Servicios que encapsulan la lógica de negocio
- `src/main/java/com/lms/educa/Factory/`: Implementación del patrón Factory para desacoplar instancias
- `src/main/resources/`: Configuración de la aplicación y scripts SQL
- `build.gradle`: Configuración del proyecto con Gradle


## 🛠️ Requisitos

- Java 17+
- Gradle 9.0.0+
- PostgreSQL

## 🚀 Ejecución

```bash
./gradlew bootRun
