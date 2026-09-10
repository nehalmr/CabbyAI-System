FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY user-service/pom.xml user-service/
WORKDIR /workspace/user-service
RUN mvn -B -DskipTests dependency:go-offline
COPY user-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/user-service/target/*.jar app.jar
USER nonroot
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
