FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/EventManagement-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080

COPY . .

ENTRYPOINT ["java","-Duser.timezone=UTC", "-Djava.library.path=/lib/" ,"-jar","/app.jar"]
