FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY payment-service/pom.xml payment-service/
WORKDIR /workspace/payment-service
RUN mvn -B -DskipTests dependency:go-offline
COPY payment-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/payment-service/target/*.jar app.jar
USER nonroot
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
