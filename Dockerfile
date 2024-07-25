FROM openjdk:21-jdk
MAINTAINER wojta
COPY target/clinic-medical-microservice-0.0.1-SNAPSHOT.jar clinic-medical-microservice.jar
ENTRYPOINT ["java", "-jar", "/clinic-medical-microservice.jar"]