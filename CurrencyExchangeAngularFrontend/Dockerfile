FROM node:14.18-alpine3.13 AS builder
WORKDIR /app
COPY package.json package-lock.json ./
COPY . .
RUN npm install
EXPOSE 4200
RUN npm run build_prod
RUN echo DONE!
CMD npm run start