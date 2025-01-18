FROM nginx:1.27.3
LABEL authors="Petre Maierean"

ENV DOMAIN_BOOT=${DOMAIN_BOOT:-training-boot}\
    DOMAIN_UI=${DOMAIN_UI:-training-ui}
COPY ./nginx/nginx.conf /etc/nginx/templates/default.conf.template
COPY ./src /usr/share/nginx/html
