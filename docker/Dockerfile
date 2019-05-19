FROM openjdk:8-alpine

ENV SERVER_PORT 8080
ENV SERVER_HOST 0.0.0.0
ENV DATABASE_URL jdbc:h2:~/test
ENV DATABASE_USER sa
ENV DATABASE_PASSWORD ""

ADD *.jar service.jar

ENTRYPOINT ["java", "-jar", "service.jar"]