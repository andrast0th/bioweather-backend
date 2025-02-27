FROM openjdk:21-jdk
MAINTAINER atoth.dev
COPY build/libs/bioweather-backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]