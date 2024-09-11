# Use an official Maven image to build the application
FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /workspace
COPY . .
RUN mvn package -DskipTests

# Use the Amazon Corretto image to run the application
FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=builder /workspace/target/high-concurrency-microservice-1.0.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]



