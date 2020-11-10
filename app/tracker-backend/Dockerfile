FROM maven:3.6.3-jdk-11-slim as builder
COPY src /app/src
WORKDIR /app
COPY pom.xml /app
RUN mvn clean package

COPY /target/family-health-tracker-backend-1.0-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
