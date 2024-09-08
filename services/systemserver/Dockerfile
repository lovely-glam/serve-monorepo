FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :systemserver -am -DskipTests

FROM openjdk:17
WORKDIR /system-server
COPY --from=builder /application-builder/services/systemserver/target/systemserver-0.0.0.jar ./systemserver.jar
ENTRYPOINT [ "java", "-jar",  "systemserver.jar" ]