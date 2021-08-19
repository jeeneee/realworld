# ![RealWorld Example Applications](docs/images/realworld-dual-mode.png)

> ### Spring Boot codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld-example-apps) API spec.

# Getting Started

## DB

```shell
$ docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql
```

## Test

```shell
$ ./gradlew test
```

To locally run the provided Postman collection against your backend, execute:

```shell
$ ./docs/run-api-tests.sh
```

For more details, see [`run-api-tests.sh`](docs/run-api-tests.sh).

- result

![result](docs/images/test-result.png)

## SonarQube

```shell
$ ./gradlew sonarqube -Dsonar.host.url=[sonarqube_url] -Dsonar.login=[token]
```

## Documentation

To generate REST API Documentation, execute:

```shell
$ ./gradlew bootJar
```

For more details, see [`api-docs.html`](src\main\resources\static\docs\api-docs.html).

# Diagram

![diagram](docs/images/diagram.png)

# License

All of the codebases are **MIT licensed** unless otherwise specified.

<br />

[![Brought to you by Thinkster](docs/images/end.png)](https://thinkster.io)
