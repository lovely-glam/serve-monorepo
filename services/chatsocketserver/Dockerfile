FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :chatsocketserver -am -DskipTests

FROM jdk:openjdk-17
WORKDIR /chatsocket-server
COPY --from=builder /application-builder/services/chatsocketserver/target/chatsocketserver-0.0.0.jar ./chatsocketserver.jar
ENTRYPOINT [ "java", "-jar",  "chatsocketserver.jar" ]