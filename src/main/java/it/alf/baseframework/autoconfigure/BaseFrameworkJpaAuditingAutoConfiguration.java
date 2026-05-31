package it.alf.baseframework.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.persistence.EntityManagerFactory;

/**
 * Enables Spring Data JPA auditing so {@code @CreatedDate} / {@code @LastModifiedDate} fields on
 * {@link it.alf.baseframework.model.BaseEntity} are populated automatically. Active whenever JPA
 * is on the classpath; can be disabled with {@code baseframework.jpa.auditing.enabled=false}.
 */
@AutoConfiguration
@ConditionalOnClass(EntityManagerFactory.class)
@ConditionalOnProperty(prefix = "baseframework.jpa.auditing", name = "enabled",
        havingValue = "true", matchIfMissing = true)
@EnableJpaAuditing
public class BaseFrameworkJpaAuditingAutoConfiguration {
}
