#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS pgcrypto CASCADE;
    CREATE USER $USER_NAME WITH
           LOGIN
           NOSUPERUSER
           INHERIT
           CREATEDB
           NOCREATEROLE
           NOREPLICATION
           NOBYPASSRLS
           PASSWORD '${USER_PASSWORD}';
    CREATE DATABASE ${USER_NAME}
           WITH
           OWNER = ${USER_NAME}
           ENCODING = 'UTF8'
           TABLESPACE = pg_default
           CONNECTION LIMIT = -1
           IS_TEMPLATE = False;
    GRANT ALL PRIVILEGES ON DATABASE ${USER_NAME} TO ${USER_NAME};
EOSQL