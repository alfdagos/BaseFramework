package it.alf.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Sample Spring Boot application that demonstrates how to wire the
 * `baseframework` library into a real microservice.
 *
 * Notes on integration:
 * - This class is a normal Spring Boot entrypoint; the sample keeps
 *   framework-specific configuration (eg. `SecurityConfig`) explicit by
 *   importing it with `@Import(...)`. This shows one way for a consumer
 *   to reuse configuration from the library while keeping the sample
 *   code and framework code separated.
 * - The sample also demonstrates the recommended pattern of creating
 *   concrete service and controller classes that extend the generic
 *   baseframework types (GenericCrudService / GenericCrudController).
 */
@SpringBootApplication
@Import(it.alf.baseframework.config.SecurityConfig.class)
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
