package it.alf.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample Spring Boot application demonstrating how to consume the {@code baseframework} library.
 *
 * <p>Notice there is no manual {@code @Import} of framework configuration: the library ships
 * Spring Boot auto-configuration (see
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}),
 * so security, the global exception handler, JPA auditing and OpenAPI are wired automatically.
 * The sample only provides concrete service/controller subclasses of the generic types.
 */
@SpringBootApplication
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
