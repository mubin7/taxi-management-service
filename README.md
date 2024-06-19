# Taxi Management Service

## Requirements

For building and running the application you need:

- JDK 21
- Gradle 8.8
- Docker

## Running the application locally

To run this Spring Boot application on your local machine. One way is to execute the `main` method in
the `com/tms/TaxiManagementServiceApplication.java` class from your IDE.

```shell
./gradlew bootRun
```

## Build docker image

To build the docker image for this Spring Boot application, please execute the following steps from the project root directory.

Build the application using the below command.
```shell
./gradlew clean build
```
Build the Docker image for this application using the below command.
```shell
docker build -t taxi-management-service .
```

## Running the application using Docker Compose
To run the application from the project root directory using Docker Compose.

Please execute the following command to run the spring boot microservice.
```shell
docker-compose up
```

## Swagger UI
Once the application is up and running, please use the following URl to access Swagger UI.

Swagger UI URL - http://localhost:8081/swagger-ui/index.html