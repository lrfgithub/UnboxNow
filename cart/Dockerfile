FROM openjdk:17-oracle
ENV TZ=America/Los_Angeles
VOLUME /tmp
MAINTAINER Eric
ADD 02-cart-v1.jar 02-cart-v1.jar
EXPOSE 8602
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","02-cart-v1.jar"]