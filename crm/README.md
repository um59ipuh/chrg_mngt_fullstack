# RESTAPI FOR CHARGING DATA MANAGEMENT
Java Spring Boot based REST API to manage Charge Data Records (CDR).

## Stack
- Java 19
- Spring Boot 3.3.1
- Maven
- H2 - Database
- Flyway DB Migration
- Docker

## Requirements
- Docker

## Installtaion in Docker
- Go to the folder of crm(Charging Data Manager) :: $cd crm/
- Run the following command **docker-compose up --build**
- this will create the neccessary migrations, seeds and boot up both backend and frontend service
- Check API endpoints on **http://localhost:8080/api/home/health**


## API Endpoints

API endpoints exposed as Swagger UI on **http://localhost:8080/swagger-ui/index.html**

## Improvements

- adding more environment like **prod**
