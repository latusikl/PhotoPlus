### STAGE 1: Build ###
FROM node:14.0.0-alpine AS build
WORKDIR /usr/src/app
COPY package.json ./
COPY package-lock.json ./
RUN npm install
RUN npm install -g @angular/cli
COPY . .
RUN ng build
#--prod=true

FROM nginx:1.17.1-alpine

COPY --from=build /usr/src/app/dist/PhotoPlus /usr/share/nginx/html
COPY nginx-custom.conf /etc/nginx/conf.d/default.conf