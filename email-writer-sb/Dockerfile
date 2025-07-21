# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Build the project
RUN ./mvnw package -DskipTests

# Run the JAR
CMD ["java", "-jar", "target/*.jar"]