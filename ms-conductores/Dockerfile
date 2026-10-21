FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --no-create-home app

COPY --from=build --chown=app:app /build/target/ms-conductores-*.jar app.jar

USER app
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
