FROM node:22-alpine AS nodes
LABEL authors="Petre Maierean"

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install
RUN npm install -g @angular/cli@17.2.0
RUN npm install -g supervisor@latest
RUN npm install @rollup/rollup-linux-x64-gnu@latest

# Copy all files
COPY . .
# Run ng build through npm to create dist folder
RUN ng build --configuration dev

EXPOSE 4200

CMD ["npm", "run", "startd"]