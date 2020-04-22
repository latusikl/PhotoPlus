FROM openjdk:11-jre-slim
RUN echo "Starting image build..."
RUN mkdir -p /usr/local/app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /usr/local/app/photoPlus.jar
ENTRYPOINT ["java","-jar","/usr/local/app/photoPlus.jar"]
