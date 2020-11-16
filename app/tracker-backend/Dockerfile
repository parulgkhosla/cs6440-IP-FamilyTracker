FROM maven:3.6.3-jdk-11-slim as builder
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
#COPY /pom.xml /app
RUN echo $(ls -1 /app)
CMD ["ls","-altr","/app"]
RUN mvn clean package

FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/family-health-tracker-backend-1.0-SNAPSHOT.jar .
EXPOSE 8086
CMD [ "java", "-jar", "family-health-tracker-backend-1.0-SNAPSHOT.jar" ]
#CMD ["ls","-altr","/app/target"]
#RUN echo $(pwd)
#COPY /target/family-health-tracker-backend-1.0-SNAPSHOT.jar /usr/local/lib/app.jar
#RUN echo $(ls -1 /app/target)
#EXPOSE 8086
#ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
