FROM maven:3.6.3-jdk-11-slim as builder
COPY src /app/src
COPY pom.xml /app
RUN mvn clean package

COPY --from=build /app/target/family-health-tracker-backend-1.0.0-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
