FROM maven:3.8-openjdk-17-slim AS builder

WORKDIR /app

COPY src /app/src
COPY pom.xml /app/pom.xml

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]