# Pro-Gramers
FP.449 - (P) Aplicación back-end con tecn. Java en serv. de apps

Aplicación de gestión de una biblioteca desarrollada en equipo. En el producto 2 se añade la base de datos MySQL, los modelos con Spring Data JPA, el CRUD completo de libros, los listados de autores, usuarios y préstamos, y la ejecución completa con Docker Compose.


## Tecnologías utilizadas:
- Java 21.
- Spring Boot 4.1.1.
- Gradle, mediante Gradle Wrapper incluido en el repositorio.
- Spring Web MVC, Thymeleaf y Bean Validation.
- Spring Data JPA (Hibernate) y MySQL 8.4.
- Docker y Docker Compose.
- VS Code con extensiones de Java, Spring Boot y Gradle.

Spring Boot utiliza Tomcat integrado como servidor web.


## Requisitos del proyecto:
- Docker Desktop con el motor iniciado. Es lo único necesario para ejecutar con Docker.
- Git.
- Para ejecutar con Gradle: JDK 21 instalado y `JAVA_HOME` y `PATH` configurados para Java 21.


## Descargar el proyecto:
Ejecutar el siguiente comando:
- git clone https://github.com/Albertlp93/Pro-Gramers.git


## Configurar las contraseñas (una sola vez):
Las contraseñas de MySQL no se guardan en el repositorio. Se crea un archivo `.env` a partir de la plantilla y se editan sus valores:
- copy .env.example .env
- notepad .env

El archivo `.env` está en `.gitignore` y no se sube a GitHub.


## Ejecutar con Docker Compose (recomendado):
Antes de empezar, iniciar Docker Desktop y comprobar que los puertos 8081 y 3307 están libres.

### 1. Construir y arrancar:
- docker compose up -d --build

El Dockerfile es multietapa: la primera etapa compila el proyecto con Gradle dentro del contenedor y la segunda ejecuta el JAR con Java 21 JRE. No hace falta compilar antes en el equipo.

Compose arranca dos servicios:
- `mysql`: MySQL 8.4. La primera vez crea las tablas con `database/schema.sql` y carga los datos de ejemplo con `database/data.sql`.
- `app`: la aplicación. Espera a que MySQL esté listo (healthcheck) antes de arrancar.

### 2. Comprobar que funciona:
- docker compose ps

Cuando `mysql` aparezca como `healthy` y `app` como `running`, acceder a:
`http://localhost:8081`

Para consultar los mensajes de la aplicación:
- docker compose logs app

### 3. Detener y volver a iniciar:
- docker compose stop
- docker compose start

### 4. Actualizar después de cambiar el código:
- docker compose up -d --build

### 5. Borrar los contenedores:
- docker compose down: elimina los contenedores y conserva los datos.
- docker compose down -v: elimina también el volumen `mysql_data`. La próxima vez se vuelven a crear las tablas y los datos de ejemplo.

Los scripts de `database/` solo se ejecutan cuando el volumen está vacío. Si se modifica `schema.sql` o `data.sql`, hay que usar `docker compose down -v` para aplicarlos.


## Ejecutar con Gradle (desarrollo):
La aplicación usa el MySQL de Docker Compose, publicado en `localhost:3307`.

### 1. Arrancar solo MySQL:
- docker compose up -d mysql

### 2. Indicar la contraseña y arrancar la aplicación (PowerShell):
- $env:DB_PASSWORD="la_misma_clave_que_BIBLIOTECA_DB_PASSWORD"
- .\gradlew.bat bootRun --console=plain

Luego, acceder a `http://localhost:8081`. Para detener la aplicación, pulsar `Ctrl + C`.

Los tests (`.\gradlew.bat test`) también necesitan MySQL arrancado y la variable `DB_PASSWORD`.


## Funcionalidades:
| Ruta | Descripción |
| --- | --- |
| `/` | Inicio con el número de libros, autores, usuarios y préstamos |
| `/libros` | Listado de libros con sus autores, y botones de editar y eliminar |
| `/libros/nuevo` | Alta de un libro con validación y selección de autores |
| `/libros/{id}/editar` | Edición de un libro |
| `/libros/{id}/eliminar` | Borrado con confirmación. No se permite si el libro tiene préstamos |
| `/autores` | Listado de autores |
| `/usuarios` | Listado de usuarios |
| `/prestamos` | Listado de préstamos con su estado (pendiente, retrasado o devuelto) |


## Modelo de datos:
- `usuarios`, `libros`, `autores` y `prestamos`.
- Un usuario realiza muchos préstamos y cada préstamo corresponde a un libro (relaciones 1:N, `@ManyToOne`).
- Un libro tiene uno o varios autores y un autor escribe uno o varios libros (relación N:M, `@ManyToMany` con la tabla intermedia `libro_autor`).


## Estructura principal del proyecto:
- `src/main/java/com/biblioteca/model/`: entidades JPA `Usuario`, `Libro`, `Autor` y `Prestamo`.
- `src/main/java/com/biblioteca/repository/`: repositorios Spring Data JPA.
- `src/main/java/com/biblioteca/controller/`: controladores MVC (inicio, CRUD de libros y listados).
- `src/main/resources/templates/`: vistas Thymeleaf y menú común en `fragments/menu.html`.
- `src/main/resources/static/css/estilos.css`: estilos de la aplicación.
- `src/main/resources/application.properties`: puerto 8081 y conexión a MySQL mediante variables de entorno.
- `database/schema.sql`: creación de las tablas a partir del diagrama E/R.
- `database/data.sql`: datos de ejemplo.
- `database/corregir_acentos.sql`: corrección de acentos para bases de datos cargadas antes de usar `utf8mb4`.
- `build.gradle`: configuración de Java, Spring Boot y dependencias.
- `Dockerfile`: imagen multietapa (compilación y ejecución).
- `.dockerignore`: archivos que no se envían a Docker al construir la imagen.
- `compose.yaml`: servicios `mysql` y `app`.
- `.env.example`: plantilla de las contraseñas.


## Trabajo colaborativo:
Cada miembro trabaja en su propia rama. Los cambios se guardan en commits y se publican en GitHub. Para incorporarlos a `main`, se abre una pull request y se revisa en equipo.

La carpeta `build/` y la caché `.gradle/` no deben incluirse en los commits. Los archivos de Gradle Wrapper sí se comparten.


## Estado actual del proyecto:
La aplicación se conecta a MySQL, muestra el inicio con contadores, permite el CRUD completo de libros y lista autores, usuarios y préstamos. Se ejecuta completa con Docker Compose.


## Documentación oficial:
- Spring Boot: https://docs.spring.io/spring-boot/
- Gradle: https://docs.gradle.org/
- Docker: https://docs.docker.com/
- Docker Compose: https://docs.docker.com/compose/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- MySQL: https://dev.mysql.com/doc/
- Git: https://git-scm.com/doc
- Eclipse Temurin: https://adoptium.net/
