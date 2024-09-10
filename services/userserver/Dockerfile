FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :userserver -am -DskipTests

FROM openjdk:17
WORKDIR /user-server
COPY --from=builder /application-builder/services/userserver/target/userserver-0.0.0.jar ./userserver.jar
ENTRYPOINT [ "java", "-jar",  "userserver.jar" ]