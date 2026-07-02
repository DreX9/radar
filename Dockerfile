# Usa una imagen oficial de Maven con Java 21 para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila el proyecto y empaqueta el .jar (saltando los tests para que sea más rápido)
RUN mvn clean package -DskipTests

# Usa una imagen más ligera de Java 21 para correr la aplicación
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/radar-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
