FROM openjdk:11.0.10-jdk-slim
EXPOSE 8080
VOLUME /tmp
MAINTAINER jeremy.rocher78@gmail.com
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
