# Using `baseframework` — patterns and walkthrough

This guide explains the recommended patterns for building a microservice on top of the
`baseframework` library, illustrated by the sample in this folder.

## What the framework gives you

Reusable building blocks that remove repetitive boilerplate from every service:
`BaseEntity`, `GenericRepository`, `GenericCrudService`, `GenericCrudController`,
`GenericMapper`, centralized `ProblemDetail` error handling, JPA auditing, a default security
filter chain, opt-in Kafka beans and OpenAPI documentation — all activated by Spring Boot
**auto-configuration**, so no manual `@Import` is needed.

## Recommended patterns

### 1. Entity

Extend `BaseEntity` to inherit the surrogate `id`, the `createdAt`/`updatedAt` auditing
timestamps and the optimistic-lock `version`. Add Bean Validation constraints on your fields.

```java
@Entity
public class MyEntity extends BaseEntity {
    @NotBlank
    private String name;
    // getters / setters
}
```

### 2. Repository

Declare an interface extending `GenericRepository<T>` to inherit the standard JPA methods
(`findAll`, `save`, `deleteById`, paging, …).

```java
public interface MyEntityRepository extends GenericRepository<MyEntity> {}
```

### 3. Service

Extend `GenericCrudService<T>` and inject your repository via the constructor. Annotate the
concrete class with `@Service`; the abstract framework class deliberately has no stereotype.
Write operations are transactional and `update`/`delete` throw `ResourceNotFoundException`
(mapped to HTTP 404) when the id does not exist.

```java
@Service
public class MyEntityService extends GenericCrudService<MyEntity> {
    public MyEntityService(MyEntityRepository repository) { super(repository); }
}
```

### 4. Controller

Extend `GenericCrudController<T>`, annotate the concrete class with `@RestController` and a
resource-specific `@RequestMapping`. The controller stays thin — it delegates to the service.

```java
@RestController
@RequestMapping("/api/my-entities")
public class MyEntityController extends GenericCrudController<MyEntity> {
    public MyEntityController(MyEntityService service) { super(service); }
}
```

Inherited endpoints: `POST` (201 + `Location`), `PUT /{id}`, `DELETE /{id}` (204),
`GET /{id}`, `GET` (list) and `GET /page` (paginated).

### 5. DTO and Mapper (optional)

Use DTOs to decouple the REST contract from the persistence model. Extend `BaseDto` and define
a MapStruct mapper extending `GenericMapper<E, D>`:

```java
@Mapper(componentModel = "spring")
public interface MyMapper extends GenericMapper<MyEntity, MyDto> {}
```

### 6. Security

The framework auto-configures an HTTP Basic `SecurityFilterChain`. Toggle it with
`baseframework.security.enabled`, or declare your own `SecurityFilterChain` bean to fully
override it (the framework backs off via `@ConditionalOnMissingBean`).

### 7. Kafka (opt-in)

Set `baseframework.kafka.enabled=true` to get a `ConsumerFactory`, `ProducerFactory` and
`KafkaTemplate` configured from the standard `spring.kafka.*` properties.

## Running the sample

```powershell
# from the project root: install the library
.\mvnw clean install

# from samples/: run tests, then the app
..\mvnw test
..\mvnw spring-boot:run
```

## Best practices

- Always create concrete service/controller subclasses of the generic types — this makes the
  generic beans resolvable by Spring and keeps the framework free of concrete beans.
- Keep infrastructure configuration (DB, Kafka) in the consuming service; the framework reads
  properties with sensible defaults but imposes nothing.
- Prefer DTO + mapper to separate the persistence model from the API model.
- Let the framework's `GlobalExceptionHandler` shape error responses; throw
  `ResourceNotFoundException` / `CustomException` (or your own, with a dedicated advice).

## FAQ

- **Can I use the framework without MapStruct?** Yes — mapping is optional; map manually or
  use another library.
- **Do I need to import any framework configuration?** No. Auto-configuration handles security,
  exception handling, JPA auditing and OpenAPI as soon as the dependency is on the classpath.
- **How do I override a default?** Declare your own bean — the framework's defaults are
  guarded by `@ConditionalOnMissingBean`.
