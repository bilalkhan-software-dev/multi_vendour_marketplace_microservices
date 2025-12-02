FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/seller-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]