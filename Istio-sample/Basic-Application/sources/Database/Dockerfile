FROM gradle:8.12-alpine

LABEL MANTAINER="Petre Maierean (pmaierean@gmail.com)"

ENV LIQUIBASE_PROJECT_DIR=${LIQUIBASE_PROJECT_DIR:-.}\
    LIQUIBASE_DATABASE_DRIVER=${LIQUIBASE_DATABASE_DRIVER:-org.postgresql.Driver}\
    LIQUIBASE_DATABASE_NAME=${LIQUIBASE_DATABASE_NAME:-postgres}\
    LIQUIBASE_DATABASE_URL=${LIQUIBASE_DATABASE_URL:-jdbc:postgresql://localhost:5432/${LIQUIBASE_DATABASE_NAME}}\
    LIQUIBASE_DATABASE_USERNAME=${LIQUIBASE_DATABASE_USERNAME:-root}\
    LIQUIBASE_DATABASE_PASSWORD=${LIQUIBASE_DATABASE_PASSWORD:-root}


ADD ${LIQUIBASE_PROJECT_DIR} /usr/local/liquibase

WORKDIR /usr/local/liquibase
USER 0
RUN mkdir -p /usr/local/liquibase/.gradle
USER $CONTAINER_USER_ID

CMD gradle update

