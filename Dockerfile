# syntax=docker/dockerfile:1.6

FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Копируем только файлы сборки (редко меняются) — это ключ к кешу
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
# (если есть) COPY gradle.properties ./

RUN chmod +x gradlew

# Прогреваем зависимости (они закешируются)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon dependencies -q || true

# Теперь копируем исходники (меняются часто — будут инвалидировать только этот слой и ниже)
COPY src/ src/

# Сборка jar (без тестов)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
