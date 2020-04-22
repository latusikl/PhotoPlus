FROM node:14.0.0-alpine AS builder
COPY ./PhotoPlus ./PhotoPlus
WORKDIR /PhotoPlus
RUN npm i
RUN $(npm bin)/ng build --prod

FROM nginx:1.15.8-alpine
COPY --from=builder /PhotoPlus/dist/PhotoPlus/ /usr/share/nginx/htm
