FROM openjdk:17-jdk-alpine

LABEL authors="silent27"

EXPOSE 5500

COPY target/MoneyTransferService-0.0.1-SNAPSHOT.jar app.jar

ADD src/main/resources/application.properties src/main/resources/application.properties

CMD ["java","-jar","app.jar"]