# Use the official Maven image as a build environment
FROM maven:3.8.3-openjdk-11 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project descriptor
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application
RUN mvn clean package

# Use the official OpenJDK 11 image as a parent image
FROM openjdk:11-jre-slim

LABEL authors="jvijay"

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application JAR file into the container at /app
COPY --from=build target/auction-0.0.1-SNAPSHOT.jmvn /app/auction.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
CMD ["java", "-jar", "auction.jar"]