FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY ride-service/pom.xml ride-service/
WORKDIR /workspace/ride-service
RUN mvn -B -DskipTests dependency:go-offline
COPY ride-service/src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/* && useradd --system --uid 1001 spring
COPY --from=build /workspace/ride-service/target/*.jar app.jar
USER 1001
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
