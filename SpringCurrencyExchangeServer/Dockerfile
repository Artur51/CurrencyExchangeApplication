# Compile stage
FROM gradle:6.9.0-jdk11 AS build
WORKDIR /app
COPY build.gradle build.gradle
COPY src src
RUN gradle build jar --no-daemon -x test
# Package stage
FROM openjdk:12-alpine AS runimage
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 80:80
ENV JAVA_OPTS="-XX:PermSize=256m -XX:MaxPermSize=500m"
ENTRYPOINT ["java","-jar","/app.jar"]
RUN echo DONE!