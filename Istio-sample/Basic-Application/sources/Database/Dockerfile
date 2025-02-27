FROM postgres:17-alpine
LABEL authors="Petre Maierean"

ENV USER_NAME={$USER_NAME:-test}\
    USER_PASSWORD={$USER_PASSWORD:-test}

COPY ./script/init-user-db.sh /docker-entrypoint-initdb.d/init-user-db.sh