# Getting Started

This project contain the following branch
* `main branch` or `redis-with-dynamodb` use dynamodb as persist layer
* `redis-mysql-testcontainer` use test container as persist layer only available in docker environment
* `redis-with-postgresql` use postgresql as persist layer

## Springboot References
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.1/maven-plugin/reference/html/#build-image)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.6.1/reference/htmlsingle/#boot-features-redis)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)

## Reactive References

https://medium.com/swlh/how-to-build-a-reactive-microservice-api-with-spring-boot-spring-webflux-and-dynamodb-using-kotlin-e1be3e99b15e

https://spring.io/guides/gs/spring-data-reactive-redis/

https://www.cnblogs.com/littleatp/p/11515458.html

https://segmentfault.com/a/1190000039133983

https://itembase.com/resources/blog/tech/spring-boot-2-best-practices-for-reactive-web-applications

https://medium.com/@cheron.antoine/reactor-java-1-how-to-create-mono-and-flux-471c505fa158

https://medium.com/pictet-technologies-blog/reactive-programming-with-spring-data-r2dbc-ee9f1c24848b

## Original requirment spec

https://drive.google.com/file/d/10AT6lbKYlUl1KJ3P2B__ziDJghSv2xaY/view

## Tech used

- Java 11 with SpringBoot WebFlux
- Maven
- BDD Cucumber Test with WireMock
- Reactive Redis
- Reactive AWS DynamoDb
- Docker and docker-compose

## How to run

1. go http://api.weatherstack.com/ and http://open-weather-map to register api key

2. First, create a new file named `application-local.yml` under the main's resources folder, with your own api keys:

   ```
   weather-stack-access-key: <your own key in step 1>
   open-weather-map-access-key: <your own key in step 1>
   ```

3. **If docker and docker-compose installed**

   Modify `docker-compose.yml` file to comment in the _awscli_ section, then

    ```shell
    $ docker-compose up -d
    ```

   and run this application from within IDE.


3. **Or with podman and podman-compose installed**

    ```shell
    $ podman-compose up -d
    ```
   Then, run the following aws command (you need to install the AWS client command first)
   _only for `main branch` or `redis-with-dynamodb`_

    ```shell
    $ aws dynamodb --endpoint-url http://localhost:8042 create-table --table-name city-weather-info --attribute-definitions AttributeName=city,AttributeType=S --key-schema AttributeName=city,KeyType=HASH --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5
    ```

   and **run this application from within IDE.**

4. To test the application, run

    ```shell
    $ curl http://localhost:8080/weather/New%20York
    ```
   