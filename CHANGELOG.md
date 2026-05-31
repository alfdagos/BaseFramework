# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Spring Boot auto-configuration**: the library now wires itself through
  `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
  Consumers no longer need manual `@Import`. New conditional auto-configurations:
  security, web/exception handling, JPA auditing, OpenAPI, and (opt-in) Kafka.
- **Auditing on `BaseEntity`**: `createdAt`, `updatedAt` (Spring Data JPA auditing) and a
  `@Version` optimistic-locking column, plus an entity-correct `equals`/`hashCode`.
- **RFC 7807 error handling**: `GlobalExceptionHandler` now returns `ProblemDetail`
  responses and handles `ResourceNotFoundException` (404), validation errors (400 with a
  per-field `errors` map) and unexpected errors (500). New `ResourceNotFoundException`.
- **Robust REST semantics** in `GenericCrudController`: `201 Created` with `Location`
  header, `404` on missing resources, `@Valid` request bodies, and a paginated
  `GET /page` endpoint.
- **Transactional service layer**: `GenericCrudService` write operations are
  `@Transactional`; `update`/`delete` validate existence; added `getById`, paginated
  `findAll(Pageable)` and `existsById`.
- **OpenAPI / Swagger UI** via springdoc, configurable through `baseframework.openapi.*`.
- **Bean Validation** support (`spring-boot-starter-validation`).
- Tooling: Maven Wrapper, JaCoCo coverage, GitHub Actions CI, configuration-property
  metadata, `LICENSE` (MIT), `CONTRIBUTING.md` and this changelog.

### Changed
- `SecurityConfig` migrated to the non-deprecated Spring Security 6 lambda DSL and made
  overridable via `@ConditionalOnMissingBean`.
- `KafkaConfig` beans are now conditional and opt-in (`baseframework.kafka.enabled=true`).
- `pom.xml` cleaned up: versions are managed by the Spring Boot BOM, `-parameters`
  compilation enabled, project version bumped to `1.0.0-SNAPSHOT`.
- README and sample documentation rewritten.

### Breaking changes
- `BaseEntity` is now `abstract` and must be subclassed.
- `GenericCrudController.findById` returns `404` (was `200` with a `null` body) and
  `create` returns `201` (was `200`).
- `GenericCrudService.update`/`delete` throw `ResourceNotFoundException` instead of
  silently up-serting / no-op deleting.

## [0.1.0] - initial

- Initial skeleton: `BaseEntity`, `GenericRepository`, `GenericCrudService`,
  `GenericCrudController`, `BaseDto`, `GenericMapper`, `CustomException` +
  `GlobalExceptionHandler`, `KafkaConfig`, `SecurityConfig`, and a sample module.
