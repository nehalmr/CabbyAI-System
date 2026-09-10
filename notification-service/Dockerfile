FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY notification-service/pom.xml notification-service/
WORKDIR /workspace/notification-service
RUN mvn -B -DskipTests dependency:go-offline
COPY notification-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/notification-service/target/*.jar app.jar
USER nonroot
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
