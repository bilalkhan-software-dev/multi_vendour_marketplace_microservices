FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/auth-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]