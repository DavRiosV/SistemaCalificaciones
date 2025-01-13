Sistema de Calificaciones

Este proyecto es una API RESTful para la gestión de alumnos y materias, con soporte de autenticación mediante JWT.

Descripción

La API permite realizar las siguientes acciones:

Crear, leer, actualizar y eliminar alumnos.

Crear, leer, actualizar y eliminar materias asociadas a alumnos.

Autenticación y autorización con JWT para proteger los endpoints.

Configuración

Requisitos

Java 21

PostgreSQL

Maven

Configuración de la Base de Datos

Asegúrate de tener una base de datos PostgreSQL configurada con los siguientes parámetros:

Nombre de la base de datos: calificaciones_db

Usuario: postgres

Contraseña: Nym3r1t0s.1108

Si deseas modificar estos valores, actualiza el archivo application.properties ubicado en src/main/resources con tus credenciales:

spring.datasource.url=jdbc:postgresql://localhost:5432/calificaciones_db
spring.datasource.username=postgres
spring.datasource.password=Nym3r1t0s.1108
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Configuración opcional de logs SQL
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

# Configuración de JWT
jwt.secret=clave_secreta_para_token
jwt.expiration=3600000

Creación de la Base de Datos
CREATE TABLE alumno (
    id SERIAL PRIMARY KEY,
    rut VARCHAR(15) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255)
);

CREATE TABLE materia (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    alumno_id BIGINT NOT NULL REFERENCES alumno(id) ON DELETE CASCADE
);

Uso de la API

Endpoints Disponibles

Alumnos

GET /api/alumnos - Obtener todos los alumnos.

POST /api/alumnos - Crear un nuevo alumno.

GET /api/alumnos/{id} - Obtener un alumno por su ID.

DELETE /api/alumnos/{id} - Eliminar un alumno.

Materias

GET /api/materias - Obtener todas las materias.

POST /api/materias - Crear una nueva materia.

GET /api/materias/{id} - Obtener una materia por su ID.

PUT /api/materias/{id} - Actualizar una materia.

DELETE /api/materias/{id} - Eliminar una materia.

GET /api/materias/alumno/{alumnoId} - Obtener materias asociadas a un alumno específico.

Autenticación JWT

POST /api/auth/signup - Registrar un nuevo usuario. Requiere un payload JSON con los campos:

{
  "name": "Nombre",
  "username": "usuario",
  "email": "correo@example.com",
  "password": "contraseña"
}

POST /api/auth/signin - Iniciar sesión. Requiere un payload JSON con los campos:

{
  "username": "usuario",
  "password": "contraseña"
}

Retorna un token JWT que debe incluirse en el encabezado Authorization para acceder a los endpoints protegidos:

Authorization: Bearer <token>

Inserción de Datos de Ejemplo

Ejecuta los siguientes comandos SQL para insertar datos iniciales:

INSERT INTO alumno (rut, nombre, direccion) VALUES
('12345678-9', 'Juan Pérez', 'Calle Falsa 123'),
('98765432-1', 'María López', 'Avenida Siempre Viva 456');

INSERT INTO materia (nombre, alumno_id) VALUES
('Matemáticas', 1),
('Ciencias', 1),
('Historia', 2);

Ejecución del Proyecto

Clona este repositorio.

Configura el archivo application.properties con tus credenciales.

Ejecuta el comando:

mvn spring-boot:run

Accede a la API en http://localhost:8080.
