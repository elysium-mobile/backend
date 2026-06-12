# Soft Work Platform - Backend

Backend desarrollado con **Spring Boot**, siguiendo una arquitectura orientada a dominios y servicios para la gestión de la plataforma.

##  Tecnologías Utilizadas

* Java 26
* Spring Boot 4
* Spring Data JPA
* Spring Security
* PostgreSQL
* JWT Authentication
* OpenAPI / Swagger
* Lombok
* Maven

---

##  Requisitos Previos

Antes de ejecutar el proyecto, asegúrese de contar con:

* Java JDK 26
* Maven
* PostgreSQL (tener una base de datos configurada)
* Un IDE compatible (IntelliJ IDEA, Eclipse o VS Code)

---

## Configuración de Base de Datos

Configure los datos de conexión en el archivo:

```properties
src/main/resources/application.properties
```

Actualice los siguientes parámetros según su entorno:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

---

## Documentación de la API

Una vez iniciada la aplicación, la documentación interactiva estará disponible en:

http://localhost:8092/swagger-ui/index.html

---

##  Arquitectura

El proyecto está organizado siguiendo buenas prácticas de desarrollo backend:

```text
src
└── main
    ├── java
    │   └── com.softworkplatform
    │       ├── application
    │       ├── domain
    │       ├── infrastructure
    │       └── interfaces
    └── resources
```
El proyecto cuenta con 7 Contextos de dominio:

* **Dashboard**: Contexto encargado de gestionar la visualización de datos y métricas relevantes para los usuarios.
* **Feedback**: Contexto dedicado a la gestión de retroalimentación entre usuarios, permitiendo la creación, visualización y respuesta a comentarios.
* **IAM**: Contexto de gestión de identidad y acceso, encargado de manejar la autenticación, autorización y administración de usuarios.
* **Notifications**: Contexto responsable de la gestión de notificaciones, incluyendo la creación, envío y seguimiento de mensajes a los usuarios.
* **Payments**: Contexto encargado de la gestión de pagos, incluyendo la integración con pasarelas de pago y el manejo de transacciones.
* **Profile Performance**: Contexto dedicado a la gestión del rendimiento de los perfiles de usuario, incluyendo la recopilación y análisis de datos para mejorar la experiencia del usuario.
* **Workers Forum**: Contexto encargado de la gestión del foro de trabajadores, permitiendo la creación, visualización y participación en discusiones entre usuarios.

### Capas principales

* **Domain**: Contiene las reglas de negocio y entidades del dominio.
* **Application**: Casos de uso y lógica de aplicación.
* **Infrastructure**: Persistencia, configuraciones y adaptadores externos.
* **Interfaces**: Controladores REST y puntos de entrada al sistema.

---

##  Estado Actual

Se estan acomodando algunos eventos instaciados.
Además, se esta verificando la implementacion de atributos establecidos en el diseño y la base de datos.
---

##  Consideraciones

* Verifique que PostgreSQL se encuentre activo antes de iniciar la aplicación.
* Configure correctamente las credenciales de acceso a la base de datos.
* Revise las configuraciones de seguridad antes de realizar despliegues en producción.

---

## Autor

Integrantes del equipo:

- Cesar Augusto Arostegui Alzamora
- Diego Andres Avalos Cordova
- Flor de Maria Contreras Leon
- Gianmarco Fabian Jiménez Guerra
- Piero Francesco Tenorio Medina

---
