#Dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /tmp
COPY target/solar-watch-0.0.1-SNAPSHOT.jar /tmp/app.jar
ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]