FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY eureka-server/pom.xml eureka-server/
WORKDIR /workspace/eureka-server
RUN mvn -B -DskipTests dependency:go-offline
COPY eureka-server/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/eureka-server/target/*.jar app.jar
USER nonroot
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
