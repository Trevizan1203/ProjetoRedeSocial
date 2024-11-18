FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

VOLUME ["/data"]

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

EXPOSE 8080
