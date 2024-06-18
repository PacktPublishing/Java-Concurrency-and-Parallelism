FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/predictive-scaling-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
