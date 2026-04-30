FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/account-service.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]