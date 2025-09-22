# ---- Build Stage ----
FROM gradle:7.6-jdk17 AS builder
WORKDIR /home/app
COPY --chown=gradle:gradle . .
ENV TZ="Europe/Moscow"
RUN date
RUN gradle clean build -x test

# ---- Production Stage ----
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /home/app/build/libs/*.jar app.jar
EXPOSE 8080

ENV TZ="Europe/Moscow"
RUN date
ENTRYPOINT ["java", "-jar", "app.jar"]