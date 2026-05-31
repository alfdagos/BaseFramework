package it.alf.baseframework.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import it.alf.baseframework.config.KafkaConfig;

/**
 * Opt-in auto-configuration for the framework's Kafka beans. Activated only when
 * {@code baseframework.kafka.enabled=true} and Spring Kafka is on the classpath, so it does not
 * interfere with Spring Boot's own Kafka auto-configuration unless explicitly requested.
 */
@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@ConditionalOnProperty(prefix = "baseframework.kafka", name = "enabled", havingValue = "true")
@Import(KafkaConfig.class)
public class BaseFrameworkKafkaAutoConfiguration {
}
