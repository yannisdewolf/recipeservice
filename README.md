# Recipeservice

Service responsible for
- creating recipes
- listing recipes
- updating recipes
- get recipes by id

Based on the BookService from Cloud Native Spring in Action book.

## Flags in the properties
| property            | values       | description                                                                                      |
|---------------------|--------------|--------------------------------------------------------------------------------------------------|
| app.fullblownrabbit | false / true | true: use rabbit integration<br/> false: use dummy message sending. No messages will be consumed |
| app.initializedata  | false / true | true: initialize test data on startup of the service                                             |                                                                                            |
| app.data.inmemory   | false / true | true: use all data in memory<br/> false: use mysql database (docker compose)                     |

## Integrations:
- Rabbit (To be removed), tests to interact with Rabbit using TestContainers.
- PostgreSQL, tests using TestContainers


## Docker

Build the image:
```bash
mvn clean verify -DskipTests && \
docker build --build-arg JAR_FILE=target/recipeservice-0.0.1-SNAPSHOT.jar -t recipe-service .
```

Run the image:
```bash
docker run -d \
  --name recipe-service \
  --net recipe-network \
  -p 9001:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://recipe-postgress:5432/cuisinedb_recipes \
  recipe-service
```

## Build and push the image via paketo: 

```bash
./mvnw spring-boot:build-image \
"-DregistryUsername=yannisdewolf" \
"-DregistryToken=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"
```

## Docker compose
A Docker compose file is included. 

# Postgres

Credentials:
user/secret

Run postgres without Compose

```bash
docker run -d \
  --name recipe-postgress \
  --net recipe-network \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=cuisinedb_recipes \
  -p 5432:5432 \
  postgres:16.1
```
# Recipeservice
(this service)

Will be reachable on port 9001

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

