# Use a Java runtime base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and project files
COPY . .

# Build the project (this will also include your React frontend in /static)
RUN ./mvnw package -DskipTests

# Run the jar
CMD ["java", "-jar", "target/*.jar"]
