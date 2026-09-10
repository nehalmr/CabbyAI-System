FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY eureka-server/pom.xml eureka-server/
WORKDIR /workspace/eureka-server
RUN mvn -B -DskipTests dependency:go-offline
COPY eureka-server/src src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN apt-get update && apt-get upgrade -y && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/* && useradd --system --uid 1001 spring
COPY --from=build /workspace/eureka-server/target/*.jar app.jar
USER 1001
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
