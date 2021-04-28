# Searcher weather

## Description
This application has the purpose to search weather for a particular city.
The system gets the data from OpenWeather provider.
Also, it displays the weather received from the provider and persists it in a database.
To optimize the number of requests to the provider, data is fetched from the database if it's not old. If the data is old, then it's updated.
Basically, data has sort of a ttl, which is configurable.
The data created/updated in the database is also sent to a messaging system to have an audit of the weather for each city.

## Technology stack
- Java 11
- Spring Boot
- MySQL (deployed in AWS)
- SQS (deployed in AWS)
- H2
- Liquibase
- OpenAPI

## Deployment
### Docker commands
```
mvn spring-boot:build-image
```
```
docker run -it -p9099:8080 searcher-weather:0.0.1
```

### OpenAPI
http://localhost:9099/swagger-ui.html