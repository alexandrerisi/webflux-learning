FROM openjdk:11
COPY . /
CMD ["./mvnw", "-Deureka.client.serviceUrl.defaultZone=http://eureka-cisco:8761/eureka/", "-Dspring.cloud.config.uri=http://config-server-cisco:8888/", "spring-boot:run"]