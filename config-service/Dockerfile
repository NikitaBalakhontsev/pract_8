FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

WORKDIR /app

COPY build/libs/config-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
