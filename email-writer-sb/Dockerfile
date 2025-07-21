# Base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Build the project
RUN ./mvnw package -DskipTests

# Run the JAR (adjust the name or use a wildcard if necessary)
CMD ["java", "-jar", "target/*.jar"]
