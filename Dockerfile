# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy M2 folder
COPY .m2 /root/.m2

# Copy the application source code to the container
COPY . .

# Download the dependencies offline to speed up the build process
RUN mvn dependency:go-offline && mvn clean package -DskipTests=true

# Stage 2: Run the application with Java 17
FROM eclipse-temurin:17-jdk-alpine AS runner

RUN mkdir /app
# Create the /app/logs directory for logs
RUN mkdir /app/logs

# Copy the generated .jar file from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

WORKDIR /app
# Expose port 5000 for the application
EXPOSE 5000

# Set the entry point to run the .jar file using Java 17
ENTRYPOINT ["java", "-jar", "app.jar"]
