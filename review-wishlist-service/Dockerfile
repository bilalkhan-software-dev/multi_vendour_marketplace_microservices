FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/review-wishlist-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]