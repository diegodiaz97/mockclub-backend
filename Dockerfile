# Etapa 1: Build de la app
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew installDist

# Etapa 2: Imagen final ligera
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/install/mockclub-backend /app
EXPOSE 8080
CMD ["./bin/mockclub-backend"]