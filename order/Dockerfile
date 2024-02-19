FROM openjdk:17-oracle
ENV TZ=America/Los_Angeles
VOLUME /tmp
MAINTAINER Eric
ADD 05-order-v1.jar 05-order-v1.jar
EXPOSE 8605
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","05-order-v1.jar"]