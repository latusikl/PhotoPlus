#building from mvn
#maven and jdk slim
FROM openjdk:11-jdk-slim as build
MAINTAINER latusikl

#Mkdir + cd
WORKDIR /usr/local/app
#source destionation
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x ./mvnw
#download dependencies and pack to jar

RUN ./mvnw clean install package -DskipTests=true

FROM openjdk:11-jdk-buster as production
WORKDIR /usr/local/prod
ARG JAR_FILE=/usr/local/app/target/photoPlussApp.jar

COPY --from=build ${JAR_FILE} /usr/local/prod/photoPlussApp.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/usr/local/prod/photoPlussApp.jar"]