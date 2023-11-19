# syntax = docker/dockerfile:1.2
FROM gradle:jdk17
LABEL authors="Ranfa"

EXPOSE 8080:8080

RUN mkdir app

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon --scan --stacktrace --refresh-dependencies --no-build-cache

WORKDIR /build/libs

CMD ["java", "-jar", "/app/build/libs/imascordhubbackend.jar"]

# Path: Dockerfile
