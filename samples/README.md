# Sample application

A small Spring Boot application demonstrating how to consume the `baseframework` library.

## Layout

```
src/main/java/it/alf/sample
├── SampleApplication.java          # plain @SpringBootApplication — no manual @Import
├── model/MyEntity.java             # extends BaseEntity, adds a validated `name`
├── dto/MyDto.java                  # extends BaseDto
├── repository/MyEntityRepository   # extends GenericRepository<MyEntity>
├── service/MyEntityService         # extends GenericCrudService<MyEntity> (@Service)
├── controller/MyEntityController   # extends GenericCrudController<MyEntity> -> /api/my-entities
└── mapper/MyMapper                 # MapStruct mapper extending GenericMapper
```

The application uses an in-memory **H2** database, so no external infrastructure is needed.
Because the framework auto-configures itself, `SampleApplication` is a plain
`@SpringBootApplication`: security, exception handling, JPA auditing and OpenAPI are wired
automatically.

## Run it

1. Install the library into your local Maven repository (from the project root):

   ```powershell
   ..\mvnw clean install
   ```

2. Start the sample (from this `samples` directory):

   ```powershell
   ..\mvnw spring-boot:run
   ```

The app starts on port **8081**.

## Try the API

```bash
# Create
curl -i -X POST http://localhost:8081/api/my-entities \
  -H "Content-Type: application/json" -d '{"name":"hello"}'

# List
curl http://localhost:8081/api/my-entities

# Fetch one (404 ProblemDetail if missing)
curl -i http://localhost:8081/api/my-entities/1

# Paginated
curl "http://localhost:8081/api/my-entities/page?page=0&size=10&sort=id,desc"
```

Useful URLs while running:

- Swagger UI: <http://localhost:8081/swagger-ui.html>
- OpenAPI document: <http://localhost:8081/v3/api-docs>
- H2 console: <http://localhost:8081/h2-console> (JDBC URL `jdbc:h2:mem:sampledb`)

## Notes

- Security is **disabled** in `application.properties` (`baseframework.security.enabled=false`)
  to keep the demo and its tests simple. Set it to `true` for HTTP Basic protection.
- The integration test (`MyEntityControllerIntegrationTest`) exercises the happy path plus the
  `404` and validation (`400`) error responses produced by the framework.
- See [DOCS.md](DOCS.md) for a deeper walkthrough of the recommended patterns.
