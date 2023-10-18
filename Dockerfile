FROM gradle AS build
WORKDIR /app
COPY src ./src
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle clean
RUN gradle bootJar

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/todo-1.0.0.jar ./
ENTRYPOINT ["java", "-jar", "todo-1.0.0.jar"]