# Builder stage
FROM maven:3.9.9 AS maven

COPY . /crm-api

WORKDIR /crm-api

RUN mvn clean install

FROM eclipse-temurin:19-jdk-focal

ARG JAR_FILE=crm-0.0.1-SNAPSHOT.jar

WORKDIR /crm-api

# Copy the built JAR file from the previous stage
COPY --from=maven crm-api/target/${JAR_FILE} /crm-api

EXPOSE 8080

CMD ["java", "-jar", "crm-0.0.1-SNAPSHOT.jar"]
