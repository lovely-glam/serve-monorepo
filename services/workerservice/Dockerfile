FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :workerservice -am -DskipTests

FROM openjdk:17
WORKDIR /worker-service
COPY --from=builder /application-builder/services/workerservice/target/workerservice-0.0.0.jar ./workerservice.jar
ENTRYPOINT [ "java", "-jar",  "workerservice.jar" ]