FROM openjdk:17-oracle
ENV TZ=America/Los_Angeles
VOLUME /tmp
MAINTAINER Eric
ADD 03-inventory-v1.jar 03-inventory-v1.jar
EXPOSE 8603
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","03-inventory-v1.jar"]