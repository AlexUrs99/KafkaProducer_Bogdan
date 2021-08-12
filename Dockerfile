FROM openjdk:8
ADD build/libs/KafkaDemo-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]
EXPOSE 8080