FROM openjdk:17-oracle
ENV TZ=America/Los_Angeles
VOLUME /tmp
MAINTAINER Eric
ADD 04-user-v1.jar 04-user-v1.jar
EXPOSE 8604
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","04-user-v1.jar"]