FROM ubuntu:latest
LABEL authors="Petre Maierean"

FROM nginx:latest
COPY ./nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY ./src /usr/share/nginx/html
