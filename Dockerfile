FROM openjdk:17
ADD target/dockerAPIGateway.jar dockerAPIGateway.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","dockerAPIGateway.jar"]