FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY payment-service/pom.xml payment-service/
WORKDIR /workspace/payment-service
RUN mvn -B -DskipTests dependency:go-offline
COPY payment-service/src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN apt-get update && apt-get upgrade -y && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/* && useradd --system --uid 1001 spring
COPY --from=build /workspace/payment-service/target/*.jar app.jar
USER 1001
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
