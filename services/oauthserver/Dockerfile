FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /application-builder
COPY . .
RUN mvn clean package -pl :oauthserver -am -DskipTests

FROM openjdk:17
WORKDIR /oauth-server
COPY --from=builder /application-builder/services/oauthserver/target/oauthserver-0.0.0.jar ./oauthserver.jar
ENTRYPOINT [ "java", "-jar",  "oauthserver.jar" ]