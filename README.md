# Pro-Gramers
FP.449 - (P) Aplicación back-end con tecn. Java en serv. de apps

Aplicación de gestión de una biblioteca desarrollada en equipo. En el producto 1 hemos preparado el entorno, el proyecto base, el control de versiones y la ejecución con Docker.


## Tecnologías utilizadas:
- Java 21.
- Spring Boot 4.1.1.
- Gradle, mediante Gradle Wrapper incluido en el repositorio.
- Spring Web MVC y Thymeleaf.
- Docker.
- VS Code con extensiones de Java, Spring Boot y Gradle.

Spring Boot utiliza Tomcat integrado como servidor web.


## Requisitos del proyecto: 
- JDK 21 instalado.
- Variables `JAVA_HOME` y `PATH` configuradas para Java 21.
- Git.
- Docker Desktop con el motor iniciado para la ejecución en Docker.


## Descargar el proyecto:
Ejecutar el siguiente comando: 
- git clone https://github.com/Albertlp93/Pro-Gramers.git


## Ejecutar el proyecto utilizando Gradle o detenerlo:
Ejecutar el siguiente comando: 
- .\gradlew.bat bootRun --console=plain

Luego, acceder a: 
`http://localhost:8081`

La terminal debe permanecer abierta. Para detener la aplicación, pulsar `Ctrl + C`.


## Ejecutar con Docker:
Antes de empezar, iniciar Docker Desktop y comprobar que el puerto 8081 está libre. Si la aplicación está ejecutándose con Gradle, detenerla.


### 1. Generar el JAR:
Ejecutar el comando: 
- .\gradlew.bat clean bootJar --console=plain

Esto genera el siguiente archivo:
`build/libs/biblioteca-0.0.1-SNAPSHOT.jar`


### 2. Construir la imagen:
Ejecutamos el siguiente comando: 
- docker build -t biblioteca:producto1 .

El Dockerfile utiliza Java 21 y copia el JAR generado.


### 3. Crear o iniciar el contenedor:
Este comando se ejecuta solo la primera vez:
- docker run -d --name biblioteca-producto1 -p 8081:8081 biblioteca:producto1

La aplicación estará disponible en:
`http://localhost:8081`


### 4. Comprobar que funciona correctamente: 
Para mostrar los contenedores activos se utiliza el comando:
- docker ps

Para consultar los mensajes de la aplicación se utiliza el comando:
- docker logs biblioteca-producto1


### 5. Detener y volver a iniciar:
Para detener el contenedor, utilizamos el comando:
- docker stop biblioteca-producto1

Volvemos a iniciar el contenedor existente con el comando:
- docker start biblioteca-producto1

Ejecutar `docker run` si ya existe un contenedor con ese nombre dará un error. 


### Actualizar el contenedor después de cambiar el código: 
Un contenedor que ya exista conserva la aplicación con la que se creó. Para utilizar los cambios, se debe generar otra vez el JAR, reconstruir la imagen y recrear el contenedor. Para ello ejecutamos: 
- .\gradlew.bat clean bootJar --console=plain
- docker build -t biblioteca:producto1 .
- docker stop biblioteca-producto1
- docker rm biblioteca-producto1
- docker run -d --name biblioteca-producto1 -p 8081:8081 biblioteca:producto1

Estos comandos eliminan únicamente el contenedor de esta aplicación y crean uno nuevo. Actualmente la aplicación no almacena datos persistentes.


## Estructura principal del proyecto:
- `src/main/java/com/biblioteca/BibliotecaApplication.java`: inicio de Spring Boot.
- `src/main/java/com/biblioteca/controller/HomeController.java`: controlador de la página inicial.
- `src/main/resources/templates/home.html`: plantilla HTML de la página inicial.
- `src/main/resources/application.properties`: configuración de la aplicación y del puerto 8081.
- `build.gradle`: configuración de Java, Spring Boot y dependencias.
- `Dockerfile`: configuración de la imagen Docker.


## Trabajo colaborativo:
Cada miembro trabaja en su propia rama. Los cambios se guardan en commits y se publican en GitHub. Para incorporarlos a `main`, se abre una pull request y se revisa en equipo.

La carpeta `build/` y la caché `.gradle/` no deben incluirse en los commits. Los archivos de Gradle Wrapper sí se comparten.


## Estado actual del proyecto: 
La aplicación muestra una página inicial y permite comprobar su ejecución local y en Docker.

Las funcionalidades de libros, usuarios y préstamos se desarrollarán en los siguientes productos.


## Documentación oficial:
- Spring Boot: https://docs.spring.io/spring-boot/
- Gradle: https://docs.gradle.org/
- Docker: https://docs.docker.com/
- Git: https://git-scm.com/doc
- Eclipse Temurin: https://adoptium.net/
