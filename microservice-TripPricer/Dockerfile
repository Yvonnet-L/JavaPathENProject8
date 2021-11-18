FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 9003
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
