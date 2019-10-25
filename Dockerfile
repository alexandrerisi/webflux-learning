FROM openjdk:11
COPY . /
ENV SPRING_APPLICATION_JSON='\
{ \
    "eureka" : { \
        "client" : { \
            "serviceUrl" : \
                { "defaultZone" : "http://eureka-cisco:8761/eureka/" \
            } \
        } \
    }, \
    "spring" : { \
        "zipkin" : { \
            "base-url" : "http://stupefied_buck:9411" \
        } \
    } \
}'
ENV RABBIT_URI=amqp://alex-rabbit:5672
CMD ["./mvnw", "-Dmaven.test.skip=true", "spring-boot:run"]


docker run -e RABBIT_URI=amqp://alex-rabbit:5672/ --net=cisco-sota -p 9411:9411 openzipkin/zipkin