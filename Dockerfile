
# Use the official OpenJDK 21 image as the base
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy Maven build files to cache dependencies
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Download dependencies without building the project
RUN ./mvnw dependency:go-offline

# Copy the rest of the application code
COPY src src

# Build the application
RUN ./mvnw clean install

# Copy the built JAR file
COPY target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "/app/app.jar"]