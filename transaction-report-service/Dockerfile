FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/transaction-report-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]