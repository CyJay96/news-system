FROM openjdk:17-jdk-slim

LABEL maintainer="Voytko K.V."

WORKDIR /app

COPY ./news-service/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
