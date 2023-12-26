# Recipeservice

Service responsible for
- creating recipes
- listing recipes
- updating recipes
- get recipes by id

Based on the BookService from Cloud Native Spring in Action book.

## TODO
- replace MySQL with PostgreSQL

## Flags in the properties
| property            | values       | description                                                                                      |
|---------------------|--------------|--------------------------------------------------------------------------------------------------|
| app.fullblownrabbit | false / true | true: use rabbit integration<br/> false: use dummy message sending. No messages will be consumed |
| app.initializedata  | false / true | true: initialize test data on startup of the service                                             |                                                                                            |
| app.data.inmemory   | false / true | true: use all data in memory<br/> false: use mysql database (docker compose)                     |

## Integrations:
- Rabbit (To be removed), tests to interact with Rabbit using TestContainers.
- MySql, tests using TestContainers


## Docker
TODO: create a Dockerfile

```bash
docker run -d \
  --name recipe-postgress \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=cuisinedb_recipes \
  -p 5432:5432 \
  postgres:16.1
```

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

