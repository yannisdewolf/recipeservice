# Recipeservice

## 

## Docker compose
A Docker compose file is included. 
When starting the service,
it will automatically launch the docker compose file and start following containers:

When stopping the service, it will automatically stop the containers.

For more info, see
https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.docker-compose

# RabbitMQ

http://localhost:15672/#/

Credentials:
guest/guest

The service will create events on the exchange `recipeservicex`

# DB admin

http://localhost:8080

# Zipkin

http://localhost:9411/zipkin/

# Metrics
http://localhost:8081/actuator/metrics
http://localhost:8081/actuator/metrics/recipe.id

