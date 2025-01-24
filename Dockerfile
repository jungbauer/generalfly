FROM openjdk:17-jdk-alpine
LABEL org.opencontainers.image.authors="jungbauer@gmail.com"
COPY build/libs/generalfly-0.0.1-SNAPSHOT.jar generalfly-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/generalfly-0.0.1-SNAPSHOT.jar"]