# Etapa 1: Construir el programa
FROM maven:3.8.6-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar el programa
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Copiar el script de inicio
COPY start.sh .

# Dar permisos de ejecución al script
RUN chmod +x start.sh

# Exponer el puerto 8080
EXPOSE 8080

# Establecer el entrypoint al script de inicio
ENTRYPOINT ["./start.sh"]
