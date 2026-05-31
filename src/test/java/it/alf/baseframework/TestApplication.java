package it.alf.baseframework;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Anchor configuration for slice tests (e.g. {@code @DataJpaTest}) in this library module,
 * which otherwise has no {@code @SpringBootConfiguration}. Not part of the published artifact.
 */
@SpringBootApplication
public class TestApplication {
}
