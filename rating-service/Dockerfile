FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY rating-service/pom.xml rating-service/
WORKDIR /workspace/rating-service
RUN mvn -B -DskipTests dependency:go-offline
COPY rating-service/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/rating-service/target/*.jar app.jar
USER nonroot
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
