# Spring Boot with Basic Auth & JWT with refresh tokens in Spring Boot Cache

Sample Spring Boot 3 application with basic auth with JWT and refresh tokens 
(stored and replicated between SpringBoot instances with Hazelcast cluster)

## Requirements
- PostgresSQL
- Java 17

## Setup
Check inside `application.yml`

#### Before start don't forget to set yours DATABASE and credentials

## Run application
`JwtApplication.Main`

## API Endpoints
Check swagger to see what you could do with this demo
http://localhost:8088/swagger-ui/index.html

## How to see it in ACTION ?
Run 2 different instances of SpringBoot with different hazelcast configurations (see `application.yml` comments)
Login and generate new JWT with refresh send to second instance of application. 

Monitor Hazelcast with [Hazelcast Managment Center in Docker container](https://docs.hazelcast.com/management-center/5.3/getting-started/get-started)

`docker run --rm -p 8080:8080 hazelcast/management-center:$MC_VERSION`


# Check my other JWT Variations: 
- [Basic JWT](https://github.com/marcinzygmunt-pl/spring-boot-jwt-basic)
- [JWT with refresh tokens in database](https://github.com/marcinzygmunt-pl/spring-boot-jwt-with-refresh)
- [JWT with refresh tokens in cache](https://github.com/marcinzygmunt-pl/spring-boot-jwt-with-refresh-cache)