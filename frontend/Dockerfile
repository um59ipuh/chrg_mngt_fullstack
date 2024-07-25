FROM node:20-alpine as build

WORKDIR /app

RUN npm install -g @angular/cli@17

COPY package*.json ./

RUN npm install

COPY . .

RUN ng build

FROM nginx:alpine

COPY --from=build /app/dist/digital-charging-record/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
