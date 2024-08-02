FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/application.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
