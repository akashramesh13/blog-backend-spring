# Use a Java base image
FROM openjdk:17-jdk-slim AS build

# Set working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Download the dependencies (using mvnw)
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application source code
COPY src /app/src

# Build the project with Maven wrapper (using mvnw)
RUN ./mvnw clean package -DskipTests

# Use the openjdk image again to run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
