# Name the node stage "builder"
FROM node:14.6 AS builder
# Set working directory
WORKDIR /app
# Copy all files from current directory to working dir in image
COPY tracker-frontend frontend
COPY config config
# install node modules and build assets
WORKDIR /app/frontend
# because our app is not at the root of the server
ENV APP_URL="/family-health-tracker"

# build the static assets
RUN yarn install && yarn build

# nginx state for serving content
FROM nginx:alpine
# Set working directory to nginx asset directory
WORKDIR /app
# Copy static assets from builder stage
COPY --from=builder /app/frontend/build html
COPY --from=builder /app/config config
RUN mkdir -p config/logs

# Container run nginx with global directives and daemon off
EXPOSE 80
CMD ["nginx", "-c", "config/nginx.conf", "-p", "/app", "-g", "daemon off;"]