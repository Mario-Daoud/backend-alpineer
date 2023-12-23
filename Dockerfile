FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar wintersports.jar
CMD ["java", "-jar", "wintersports.jar"]
