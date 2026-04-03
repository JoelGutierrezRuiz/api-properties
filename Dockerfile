# Etapa 1: Construcción del JAR
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY . .
RUN ./mvnw -B -DskipTests package

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
