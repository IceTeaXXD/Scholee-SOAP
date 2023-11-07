FROM maven:3.8-openjdk-11-slim

WORKDIR /app

COPY SOAP/src ./src
COPY SOAP/pom.xml .
COPY SOAP/.env /app/.env

RUN mvn clean package

CMD ["mvn", "exec:java"]