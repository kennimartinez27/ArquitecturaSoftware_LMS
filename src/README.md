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


1. Fábricas (Factory Method): - Hay una interfaz UsuarioFactory y fábricas concretas para cada rol (AdministradorFactory, ProfesorFactory, EstudianteFactory). - Cada fábrica implementa el método crearUsuario, devolviendo la instancia correspondiente. - Esto cumple con el patrón Factory Method y permite agregar nuevos roles sin modificar el resto del sistema.

2. Fachada administrativa (Facade): - La clase AdminFacade expone métodos simples para crear y listar usuarios, materias, contenidos y reportes. - Internamente delega en los servicios especializados, desacoplando la lógica de orquestación de los controladores. - La implementación es correcta y sigue el patrón Facade.

3. Observer (Suscriptores y notificaciones): - En ObserverTest se muestra cómo Materia, Contenido y Foro actúan como sujetos, y los actores como observadores. - Se usan métodos como addObserver y notifyObservers, lo que indica una correcta aplicación del patrón Observer. - La estructura permite notificar a distintos tipos de observadores ante eventos relevantes.