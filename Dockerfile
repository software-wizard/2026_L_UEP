FROM maven:3.8.6-eclipse-temurin-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl game-server -am -DskipTests
FROM eclipse-temurin:11-alpine
WORKDIR /app
COPY --from=build /app/game-server/target/game-server-1.0-SNAPSHOT.jar /app/game-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "game-server.jar"]
