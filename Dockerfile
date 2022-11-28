FROM maven:3-amazoncorretto-8 AS build

WORKDIR /app

COPY pom.xml ./

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean install assembly:single

# package the application
FROM amazoncorretto:8

WORKDIR /app

COPY --from=build /app/target/binotify-soap-jar-with-dependencies.jar /app/app.jar

EXPOSE 3002

ENTRYPOINT ["java","-jar","app.jar"]