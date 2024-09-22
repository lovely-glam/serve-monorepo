FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :nailserver -am -DskipTests

FROM openjdk:17
WORKDIR /nail-server
COPY --from=builder /application-builder/services/nailserver/target/nailserver-0.0.0.jar ./nailserver.jar
ENTRYPOINT [ "java", "-jar",  "nailserver.jar" ]