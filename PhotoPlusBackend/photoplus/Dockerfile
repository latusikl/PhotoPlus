##Building JAR##
FROM openjdk:11-jdk-slim as build
MAINTAINER latusikl
#mkdir + cd
WORKDIR /usr/local/app
#Copy necessary files to container.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw

#Download dependencies and pack to jar.
RUN ./mvnw clean install package -DskipTests=true

#Create final container.
FROM openjdk:11-jdk-buster as production

WORKDIR /usr/local/prod
ARG JAR_FILE=/usr/local/app/target/photoPlussApp.jar

COPY --from=build ${JAR_FILE} /usr/local/prod/photoPlussApp.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/usr/local/prod/photoPlussApp.jar"]