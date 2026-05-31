package it.alf.baseframework.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Import;

import io.swagger.v3.oas.models.OpenAPI;
import it.alf.baseframework.config.OpenApiConfig;

/**
 * Auto-configures the default {@link OpenAPI} document for servlet web applications when
 * springdoc/swagger is on the classpath.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(OpenAPI.class)
@Import(OpenApiConfig.class)
public class BaseFrameworkOpenApiAutoConfiguration {
}
