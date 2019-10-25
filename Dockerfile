FROM openjdk:11
COPY . /
ENV SPRING_APPLICATION_JSON='{ "eureka" : \
    { "client" : \
        { "serviceUrl" : \
            { "defaultZone" : "http://eureka-cisco:8761/eureka/"} \
        } \
    } \
}'
ENV RABBIT_URI=amqp://alex-rabbit:5672
CMD ["./mvnw", "-Dmaven.test.skip=true", "spring-boot:run"]