# syntax=docker/dockerfile:1

FROM openjdk:16-alpine3.13 

WORKDIR /app

COPY src/challenge/.mvn/ .mvn
COPY src/challenge/mvnw src/challenge/pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src/challenge/src ./src

COPY data/* .src/main/resources/input/

CMD ["./mvnw", "spring-boot:run"]
