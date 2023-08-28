FROM maven:3.9.1-eclipse-temurin-11-alpine
WORKDIR /app
COPY ./dominiondiamondhotel-api ./dominiondiamondhotel-api
COPY ./dominiondiamondhotel-boot ./dominiondiamondhotel-boot
COPY ./dominiondiamondhotel-model ./dominiondiamondhotel-model
COPY ./dominiondiamondhotel-ws ./dominiondiamondhotel-ws
COPY pom.xml .
EXPOSE 33333
RUN mvn install
WORKDIR /app/dominiondiamondhotel-boot
CMD mvn spring-boot:run