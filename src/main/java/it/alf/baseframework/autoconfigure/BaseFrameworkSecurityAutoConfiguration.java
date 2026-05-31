package it.alf.baseframework.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.SecurityFilterChain;

import it.alf.baseframework.config.SecurityConfig;

/**
 * Auto-configures the framework's default {@link SecurityFilterChain} for servlet web
 * applications that have Spring Security on the classpath. Consumers do not need to import or
 * component-scan the {@code config} package.
 */
@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import(SecurityConfig.class)
public class BaseFrameworkSecurityAutoConfiguration {
}
