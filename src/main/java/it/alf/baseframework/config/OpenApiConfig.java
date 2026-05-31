package it.alf.baseframework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Default OpenAPI / Swagger UI configuration. The title, description and version are driven by
 * {@code baseframework.openapi.*} properties so each microservice can describe itself without
 * writing boilerplate. A {@code basicAuth} security scheme is declared so Swagger UI offers an
 * authentication dialog consistent with the framework's default HTTP Basic security.
 *
 * <p>When the framework is on the classpath, Swagger UI is served at {@code /swagger-ui.html}
 * and the OpenAPI document at {@code /v3/api-docs}.
 */
@Configuration
public class OpenApiConfig {

    @Value("${baseframework.openapi.title:Microservice API}")
    private String title;

    @Value("${baseframework.openapi.description:REST API powered by BaseFramework}")
    private String description;

    @Value("${baseframework.openapi.version:v1}")
    private String version;

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI baseFrameworkOpenAPI() {
        final String basicAuth = "basicAuth";
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version))
                .components(new Components()
                        .addSecuritySchemes(basicAuth, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList(basicAuth));
    }
}
