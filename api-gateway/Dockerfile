FROM openjdk:17-jdk-slim

LABEL maintainer="Voytko K.V."

WORKDIR /app

COPY ./api-gateway/build/libs/*.jar /app/app.jar
COPY ./wait-for-it.sh /app

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
