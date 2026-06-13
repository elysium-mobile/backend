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


## Datos de Prueba (Seed)

Para facilitar las pruebas locales, se incluye un archivo SQL con datos precargados que cubre todos los contextos de dominio de la plataforma.

### Archivo (provisional)
Se aloja de manera porvisional hasta el despliegue de la aplicación en un entorno de pruebas, donde se podrá acceder directamente desde el repositorio.

https://drive.google.com/uc?export=download&id=1KMZ-u6C9M62CdayNSHqFRIJeuYjkzWlA

### Escenario de datos

| Empresa | RUC |
|---|---|
| TechCorp SAC | 20123456781 |
| InnovatePeru SRL | 20987654322 |

### Credenciales de acceso

| Email | Contraseña | Rol | Empresa |
|---|---|---|---|
| carlos.ramirez@techcorp.pe | carlos123 | Backend Developer / RRHH | TechCorp |
| maria.lopez@techcorp.pe | maria123 | Frontend Developer | TechCorp |
| jorge.quispe@techcorp.pe | jorge123 | QA Engineer | TechCorp |
| diego.chavez@techcorp.pe | diego123 | Tech Lead | TechCorp |
| valeria.mendoza@techcorp.pe | valeria123 | Junior Developer | TechCorp |
| ana.torres@innovateperu.com | ana123 | Product Manager / RRHH | InnovatePeru |
| luis.mamani@innovateperu.com | luis123 | Data Analyst | InnovatePeru |
| sofia.vargas@innovateperu.com | sofia123 | DevOps Engineer | InnovatePeru |

### Datos incluidos por contexto

| Contexto | Datos cargados |
|---|---|
| **IAM** | 8 usuarios, 8 cuentas, 8 perfiles de empleado, 2 perfiles RRHH |
| **Dashboard** | 2 dashboards, 4 widgets |
| **Workers Forum** | 2 foros, 4 categorías, 4 threads, 7 mensajes, 4 archivos adjuntos |
| **Payments** | 3 membresías, 3 planes, 6 beneficios, 3 órdenes, 3 pagos |
| **Notifications** | 6 notificaciones, 6 detalles |
| **Profile Performance** | 5 evaluaciones de rendimiento, 5 comentarios RRHH |
| **Feedback** | 3 encuestas, 5 preguntas, 5 respuestas |

---


##  Consideraciones

* Verifique que PostgreSQL se encuentre activo antes de iniciar la aplicación.
* Configure correctamente las credenciales de acceso a la base de datos.
* Revise las configuraciones de seguridad antes de realizar despliegues en producción.

---

## Autor

Integrantes del equipo:

- Cesar Augusto Arostegui Alzamora (U202114548)
- Diego Andres Avalos Cordova (U202313922)
- Flor de Maria Contreras Leon (U202323243)
- Gianmarco Fabian Jiménez Guerra (U202123843)
- Piero Francesco Tenorio Medina (U202318731)

---
