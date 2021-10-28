FROM openjdk:11.0.10-jre-slim

EXPOSE 8080
VOLUME /tmp
MAINTAINER jeremy.rocher78@gmail.com

ARG USER_NAME=javauser
RUN addgroup --system ${USER_NAME} && adduser -S -s /bin/false -G ${USER_NAME} ${USER_NAME}

ARG DIRECTORY_PROJECT=/project
RUN mkdir ${DIRECTORY_PROJECT}

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ${DIRECTORY_PROJECT}/app.jar
WORKDIR ${DIRECTORY_PROJECT}

USER ${USER_NAME}

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]
