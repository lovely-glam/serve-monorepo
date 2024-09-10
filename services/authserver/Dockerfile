FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :authserver -am -DskipTests

FROM openjdk:17
WORKDIR /auth-server
COPY --from=builder /application-builder/services/authserver/target/authserver-0.0.0.jar ./authserver.jar
ENTRYPOINT [ "java", "-jar",  "authserver.jar" ]