# RediSolar for Java

In order to start and run this application, you will need:

* Java 8 JDK or higher
* Maven
* Access to a local or remote installation of [Redis](https://redis.io/download) version 5 or newer (local preferred)
* RedisTimeSeries Installation [RedisTimeSeries Module](https://oss.redislabs.com/redistimeseries/) installed


How to Start the RediSolar Application
---

### When using Redis on localhost, port 6379 with no password:

1. Run `mvn package` to build your application.
2. Load the sample data: `java -jar target/redisolar-1.0.jar load`.  If you want to erase everything in Redis before loading the data, use `java -jar target/redisolar-1.0.jar load --flush true`, but be aware that this will delete ALL keys in your Redis database.
3. To check that your application is running enter url `http://localhost:8081`, substituting `localhost` for the hostname that you're running the application on if necessary.

### When using Redis on another host, port or with a password:

1. Edit `config.yml`, setting the values for your Redis host, port and password if needed.
2. Edit `src/test/java/com/redislabs/university/RU102J/HostPort.java`, setting the values for your Redis host, port, and password if needed.
3. Run `mvn package` to build your application.
4. Load the sample data with `java -jar target/redisolar-1.0.jar load --host <hostname> --port <port> --password <password>`.
5. Start application with `java -jar target/redisolar-1.0.jar server config.yml`.
6. To check that your application is running enter url `http://localhost:8081`, substituting `localhost` for the hostname that you're running the application on if necessary.

Tests
---

To run all tests:

```
mvn test
```

To run a specific test:

```
mvn test -Dtest=JedisBasicsTest
```

Building
---

To rebuild the application:

```
mvn package
```

To rebuild the application without running the tests:

```
mvn package -DskipTests 
```
