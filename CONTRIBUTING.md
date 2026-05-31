# Contributing to BaseFramework

Thanks for taking the time to contribute! This document describes how to build, test and
propose changes.

## Prerequisites

- JDK 21 (the build targets Java 21 via `--release`).
- No local Maven installation required — use the included wrapper (`./mvnw` / `mvnw.cmd`).

## Building and testing

```bash
# Build the library and run all tests
./mvnw clean verify

# Install it into your local Maven repository (needed before building the sample)
./mvnw clean install

# Build and run the sample application
cd samples
../mvnw test          # runs the MockMvc integration tests
../mvnw spring-boot:run
```

A JaCoCo coverage report is generated at `target/site/jacoco/index.html` during `verify`.

> Dependencies are resolved from Maven Central only. The wrapper uses the project-local
> `.mvn/settings.xml` (configured in `.mvn/maven.config`), so the build does not depend on any
> user-level or corporate Maven configuration.

## Coding guidelines

- Keep the framework free of concrete Spring beans where the consumer should decide: prefer
  abstract base types (`GenericCrudService`, `GenericCrudController`) and `@ConditionalOnMissingBean`.
- New configuration must be exposed through Spring Boot **auto-configuration** (add a class in
  `it.alf.baseframework.autoconfigure` and register it in
  `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`), guarded by
  appropriate `@ConditionalOn*` annotations.
- Document new configuration properties in
  `src/main/resources/META-INF/additional-spring-configuration-metadata.json`.
- Every change should come with tests. Prefer fast unit tests; use `ApplicationContextRunner`
  for auto-configuration behaviour and slice tests (`@DataJpaTest`) for persistence concerns.

## Commit / PR conventions

- Use clear, imperative commit messages (e.g. "Add pagination endpoint to generic controller").
- Update `CHANGELOG.md` under the `[Unreleased]` section.
- Follow [Semantic Versioning](https://semver.org/) for releases.

## Reporting issues

Please include the framework version, Spring Boot version, a minimal reproduction and the
relevant stack trace / `ProblemDetail` response.
