FROM maven:3.6.3-openjdk-17 AS build-stage
WORKDIR /app

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn --batch-mode clean package


FROM openjdk:17.0.1-slim as run-stage
WORKDIR /app
COPY --from=build-stage /app/target/spotifygazer-*.jar /app/spotifygazer.jar
EXPOSE 6000
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar spotifygazer.jar"]
