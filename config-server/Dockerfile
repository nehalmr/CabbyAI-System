FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY config-server/pom.xml config-server/
WORKDIR /workspace/config-server
RUN mvn -B -DskipTests dependency:go-offline
COPY config-server/src src
RUN mvn -B -DskipTests package

FROM gcr.io/distroless/java21-debian12
WORKDIR /app
COPY --from=build /workspace/config-server/target/*.jar app.jar
USER nonroot
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
