FROM openjdk:11
COPY . /
ENV SPRING_APPLICATION_JSON='{ "eureka" : \
    { "client" : \
        { "serviceUrl" : \
            { "defaultZone" : "http://eureka-cisco:8761/eureka/"} \
        } \
    } \
}'
CMD ["./mvnw", "-Dmaven.test.skip=true", "spring-boot:run"]