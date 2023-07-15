FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/EventManagement-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080

RUN apk add --no-cache fontconfig ttf-dejavu

ENTRYPOINT ["java","-Duser.timezone=UTC","-jar","/app.jar"]
