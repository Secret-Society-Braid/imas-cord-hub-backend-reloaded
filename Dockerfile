# syntax = docker/dockerfile:1.2
FROM openjdk:17.0.2-jdk AS build
LABEL authors="Ranfa"

RUN mkdir app

WORKDIR /app

COPY . .

RUN ./gradlew bootJar --no-daemon --scan --stacktrace --refresh-dependencies --no-build-cache

WORKDIR /build/libs

CMD ["java", "-jar", "imascordhubbackend.jar"]

# Path: Dockerfile