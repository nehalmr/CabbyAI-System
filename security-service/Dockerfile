FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY security-service/pom.xml security-service/
WORKDIR /workspace/security-service
RUN mvn -B -DskipTests dependency:go-offline
COPY security-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/security-service/target/*.jar app.jar
USER nonroot
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
