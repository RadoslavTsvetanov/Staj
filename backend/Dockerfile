# Use an official Gradle image with JDK 21
FROM gradle:8.10.0-jdk21

# Set the working directory in the container
WORKDIR /app

# Copy the entire Spring Boot project into the container
COPY . .

# Expose the application port (optional, adjust as needed)
EXPOSE 4550

# Run the Spring Boot application using bootRun
ENTRYPOINT ["gradle", "bootRun"]
