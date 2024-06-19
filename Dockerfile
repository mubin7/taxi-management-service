FROM openjdk:21-jdk-slim
WORKDIR /app
ENV PORT 8081
EXPOSE 8081
COPY build/libs/taxi-management-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]