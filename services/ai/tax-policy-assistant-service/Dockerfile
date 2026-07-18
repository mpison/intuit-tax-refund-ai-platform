FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY   --from=build   /workspace/target/tax-policy-assistant-service-0.4.0.jar   /app/application.jar

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
