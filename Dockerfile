FROM eclipse-temurin:17
WORKDIR workspace
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} recipe-service.jar

ENTRYPOINT ["java", "-jar", "recipe-service.jar", "--spring.docker.compose.enabled=false"]