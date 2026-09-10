FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY driver-service/pom.xml driver-service/
WORKDIR /workspace/driver-service
RUN mvn -B -DskipTests dependency:go-offline
COPY driver-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/driver-service/target/*.jar app.jar
USER nonroot
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
