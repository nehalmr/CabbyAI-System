FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
WORKDIR /workspace/api-gateway
RUN mvn -B -DskipTests dependency:go-offline
COPY api-gateway/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/*.jar app.jar
USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
