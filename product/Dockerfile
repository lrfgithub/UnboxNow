FROM openjdk:17-oracle
ENV TZ=America/Los_Angeles
VOLUME /tmp
MAINTAINER Eric
ADD 01-product.jar 01-product.jar
EXPOSE 8601
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","01-product-v1.jar"]