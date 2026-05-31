package it.alf.baseframework.autoconfigure;

import it.alf.baseframework.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Validates the conditional behavior of the framework auto-configurations using the
 * Spring Boot test context runners (no full application bootstrap required).
 */
class AutoConfigurationTest {

    private final WebApplicationContextRunner webRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    BaseFrameworkSecurityAutoConfiguration.class,
                    BaseFrameworkWebAutoConfiguration.class));

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(BaseFrameworkKafkaAutoConfiguration.class));

    @Test
    void exceptionHandlerIsRegisteredForWebApplications() {
        webRunner.run(context -> assertThat(context).hasSingleBean(GlobalExceptionHandler.class));
    }

    @Test
    void securityFilterChainIsRegisteredForWebApplications() {
        webRunner.run(context -> assertThat(context).hasSingleBean(SecurityFilterChain.class));
    }

    @Test
    void kafkaBeansAreNotRegisteredByDefault() {
        runner.run(context -> assertThat(context).doesNotHaveBean(KafkaTemplate.class));
    }

    @Test
    void kafkaBeansAreRegisteredWhenEnabled() {
        runner.withPropertyValues("baseframework.kafka.enabled=true")
                .run(context -> assertThat(context).hasSingleBean(KafkaTemplate.class));
    }
}
