FROM eclipse-temurin:21
LABEL authors="Petre Maierean"

ARG VERSION=${JAR_VERSION:-0.0.2-SNAPSHOT}

WORKDIR /opt/app
COPY ./build/libs/Sample-$VERSION.jar ./Sample.jar
ENV FILE_PATH=${FILE_PATH:-/opt/app/local/sample/storage.csv}\
    DATABASE_USER=${DATABASE_USER}\
    DATABASE_PASSWORD=${DATABASE_PASSWORD}\
    DATABASE_URL=${DATABASE_URL:-jdbc:postgresql://postgresql:5432/postgres}

RUN mkdir -p /opt/app/local/sample

EXPOSE 5009
EXPOSE 8080

CMD ["java","-agentlib:jdwp=transport=dt_socket,address=*:5009,server=y,suspend=n", "-Dspring.profiles.active=dev", "-jar", "Sample.jar"]
