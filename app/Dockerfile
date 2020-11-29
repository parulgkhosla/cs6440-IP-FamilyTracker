# Name the node stage "builder"
FROM node:14.6 AS builder

# Set working directory
WORKDIR /app
# copy files from project to the image
COPY tracker-frontend frontend

WORKDIR /app/frontend
# to do or not?
#ENV PUBLIC_URL="/family-health-tracker"

# build
RUN yarn install && yarn build

# nginx state for serving content
FROM nginx:alpine
# Set working directory to nginx asset directory
WORKDIR /app
COPY --from=builder /app/frontend/build /usr/share/nginx/html/
RUN echo $(ls -1 /usr/share/nginx/html/)
EXPOSE 80
ENTRYPOINT ["nginx","-g","daemon off;"]