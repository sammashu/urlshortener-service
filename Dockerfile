FROM adoptopenjdk/openjdk8:alpine
VOLUME /urlshortener-service
ARG JAR_FILE=target/urlshortener-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} urlshortener-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/urlshortener-service.jar"]