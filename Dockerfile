# Etapa 1: Construir el programa
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar el programa
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar openssl para crear el keystore PKCS12
RUN apk add --no-cache openssl

# Copiar el JAR construido
COPY --from=build /app/target/*.jar app.jar

# Copiar el script de inicio
COPY start.sh .

# Dar permisos de ejecución al script
RUN chmod +x start.sh

# Exponer el puerto 8080
EXPOSE 8080

# Establecer el entrypoint al script de inicio
ENTRYPOINT ["sh", "./start.sh"]
