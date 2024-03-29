FROM eclipse-temurin:21-jre AS builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} recipe-service.jar
RUN java -Djarmode=layertools -jar recipe-service.jar extract

FROM eclipse-temurin:21-jre
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./


ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]