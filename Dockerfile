# Etapa 1: compila la aplicación con el JDK y Gradle Wrapper
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src

# Primero los ficheros de Gradle para reutilizar la caché de dependencias
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew \
    && ./gradlew dependencies --no-daemon > /dev/null

# Después el código fuente y la generación del JAR
COPY src src
RUN ./gradlew bootJar --no-daemon

# Etapa 2: imagen final solo con el JRE y el JAR
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /src/build/libs/biblioteca-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
