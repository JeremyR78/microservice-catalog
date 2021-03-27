FROM openjdk:11.0.10-jdk-slim
VOLUME /tmp
MAINTAINER jeremy.rocher78@gmail.com
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
