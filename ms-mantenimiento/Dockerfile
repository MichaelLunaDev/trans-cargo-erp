# Etapa 1: compilación con Maven (no se usa mvnw, está ignorado en git)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# primero solo el pom para cachear las dependencias en una capa propia
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

# Etapa 2: imagen final solo con el JRE
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --no-create-home app

COPY --from=build --chown=app:app /build/target/ms-mantenimiento-*.jar app.jar

USER app
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
