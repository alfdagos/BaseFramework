# BaseFramework — Common Library for Spring Boot Microservices

[![CI](https://github.com/alf/baseframework/actions/workflows/ci.yml/badge.svg)](.github/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)

`baseframework` is a reusable library (packaging: `jar`) that provides ready-made building
blocks for Spring Boot microservices: generic CRUD layers, shared infrastructure
configuration (Security, Kafka), JPA auditing, centralized RFC 7807 error handling and OpenAPI
documentation — all wired automatically through **Spring Boot auto-configuration**.

> Drop the dependency in and you get a consistent, opinionated baseline across every service,
> with almost no boilerplate to repeat.

---

## Table of contents

- [Highlights](#highlights)
- [Requirements](#requirements)
- [Getting started](#getting-started)
- [Components](#components)
- [Configuration properties](#configuration-properties)
- [REST contract](#rest-contract)
- [Error format (RFC 7807)](#error-format-rfc-7807)
- [Sample application](#sample-application)
- [Building from source](#building-from-source)
- [License](#license)

## Highlights

- 🧩 **Generic CRUD stack** — `BaseEntity`, `GenericRepository`, `GenericCrudService`,
  `GenericCrudController` and a `GenericMapper` (MapStruct) contract.
- ⚙️ **Zero-wiring** — auto-configuration enables security, exception handling, JPA auditing
  and OpenAPI without any manual `@Import` or component scanning.
- 🕓 **Auditing & optimistic locking** — `createdAt`, `updatedAt` and `@Version` on every entity.
- 🛡️ **Sensible security default** — HTTP Basic on by default, toggleable per environment.
- 🚦 **Consistent errors** — a single `@RestControllerAdvice` returning `ProblemDetail`.
- 📖 **OpenAPI/Swagger UI** out of the box, configurable via properties.
- ✅ **Tested** — unit, auto-configuration (`ApplicationContextRunner`) and JPA slice tests,
  with JaCoCo coverage.

## Requirements

| Tool        | Version |
|-------------|---------|
| Java        | 21      |
| Spring Boot | 3.5.7   |
| Maven       | use the bundled wrapper (`./mvnw`) |

## Getting started

### 1. Add the dependency

```xml
<dependency>
    <groupId>it.alf</groupId>
    <artifactId>baseframework</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Install it locally first (until it is published to a repository):

```bash
./mvnw clean install
```

### 2. Create your domain types

```java
@Entity
public class MyEntity extends BaseEntity {
    @NotBlank
    private String name;
    // getters / setters
}

public interface MyEntityRepository extends GenericRepository<MyEntity> {}
```

### 3. Extend the generic service and controller

Concrete subclasses let Spring resolve the generic beans (generics are erased at runtime) and
let each resource own its path:

```java
@Service
public class MyEntityService extends GenericCrudService<MyEntity> {
    public MyEntityService(MyEntityRepository repository) {
        super(repository);
    }
}

@RestController
@RequestMapping("/api/my-entities")
public class MyEntityController extends GenericCrudController<MyEntity> {
    public MyEntityController(MyEntityService service) {
        super(service);
    }
}
```

That's it — the CRUD endpoints, validation, error handling, auditing, security and Swagger UI
are all active. **No `@Import` of framework configuration is required.**

## Components

| Component | Description |
|-----------|-------------|
| `model.BaseEntity` | `@MappedSuperclass` with generated `id`, auditing timestamps, `@Version` and id-based `equals`/`hashCode`. Abstract — extend it. |
| `repository.GenericRepository<T>` | `JpaRepository<T, Long>` for every entity. |
| `service.GenericCrudService<T>` | Abstract, transactional CRUD service. `update`/`delete` validate existence; provides `getById`, `findAll(Pageable)`, `existsById`. |
| `controller.GenericCrudController<T>` | Abstract REST controller (POST/PUT/DELETE/GET, list and paged). Not annotated — the concrete subclass owns `@RestController` + `@RequestMapping`. |
| `dto.BaseDto` / `mapper.GenericMapper<E,D>` | DTO base type and MapStruct mapping contract. |
| `exception.GlobalExceptionHandler` | Centralized advice returning `ProblemDetail`. |
| `exception.ResourceNotFoundException` / `CustomException` | Mapped to 404 / 400. |
| `config.SecurityConfig` | Default HTTP Basic security (lambda DSL), overridable. |
| `config.KafkaConfig` | Opt-in String consumer/producer factories + `KafkaTemplate`. |
| `config.OpenApiConfig` | Property-driven OpenAPI document with HTTP Basic scheme. |
| `autoconfigure.*` | Conditional auto-configurations registered in `AutoConfiguration.imports`. |

## Configuration properties

| Property | Default | Description |
|----------|---------|-------------|
| `baseframework.security.enabled` | `true` | When `false`, all requests are permitted (dev/gateway scenarios). |
| `baseframework.kafka.enabled` | `false` | Opt-in flag that activates the framework's Kafka beans. |
| `baseframework.jpa.auditing.enabled` | `true` | Enables `createdAt`/`updatedAt` population. |
| `baseframework.openapi.title` | `Microservice API` | Title shown in OpenAPI / Swagger UI. |
| `baseframework.openapi.description` | `REST API powered by BaseFramework` | OpenAPI description. |
| `baseframework.openapi.version` | `v1` | OpenAPI version label. |

When Kafka is enabled, the usual `spring.kafka.bootstrap-servers`,
`spring.kafka.consumer.group-id` and `spring.kafka.producer.acks` properties apply.

## REST contract

For a controller mapped to `/api/my-entities`:

| Method & path | Behaviour | Success status |
|---------------|-----------|----------------|
| `POST /api/my-entities` | Create (validated body) | `201 Created` + `Location` |
| `PUT /api/my-entities/{id}` | Replace existing (404 if missing) | `200 OK` |
| `DELETE /api/my-entities/{id}` | Delete (404 if missing) | `204 No Content` |
| `GET /api/my-entities/{id}` | Fetch one (404 if missing) | `200 OK` |
| `GET /api/my-entities` | List all | `200 OK` |
| `GET /api/my-entities/page?page=0&size=20&sort=id,desc` | Paginated list | `200 OK` |

Swagger UI is available at `/swagger-ui.html` and the OpenAPI document at `/v3/api-docs`.

## Error format (RFC 7807)

Every error is returned as an `application/problem+json` `ProblemDetail`:

```json
{
  "type": "about:blank",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "MyEntity with id '999' was not found",
  "timestamp": "2026-05-31T10:15:30Z"
}
```

Validation failures additionally include a per-field `errors` map:

```json
{
  "title": "Validation Failed",
  "status": 400,
  "detail": "One or more fields are invalid.",
  "errors": { "name": "name must not be blank" }
}
```

## Sample application

A runnable demo lives in [`samples/`](samples/README.md): a Spring Boot app using H2 that wires
`MyEntity` through the framework and exposes `/api/my-entities`. See its README for run
instructions.

## Building from source

```bash
./mvnw clean verify          # compile, test, JaCoCo coverage report
./mvnw clean install         # install into the local Maven repository
```

Coverage report: `target/site/jacoco/index.html`.

> The build is self-contained: [`.mvn/settings.xml`](.mvn/settings.xml) (wired via
> [`.mvn/maven.config`](.mvn/maven.config)) makes the wrapper resolve everything from Maven
> Central, independently of any user-level `~/.m2/settings.xml`.

See [CONTRIBUTING.md](CONTRIBUTING.md) for development guidelines and [CHANGELOG.md](CHANGELOG.md)
for the version history.

## License

Released under the [MIT License](LICENSE).
