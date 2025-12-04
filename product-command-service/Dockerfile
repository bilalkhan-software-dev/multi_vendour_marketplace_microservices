FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/product-command-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]