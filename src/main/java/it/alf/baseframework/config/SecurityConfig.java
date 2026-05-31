package it.alf.baseframework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Default security configuration provided by the framework.
 *
 * <p>By default every request requires authentication via HTTP Basic. Set
 * {@code baseframework.security.enabled=false} to permit all requests (useful for local
 * development or when security is handled at the gateway). Because the bean is annotated
 * with {@link ConditionalOnMissingBean}, a consumer can fully override it by declaring its
 * own {@link SecurityFilterChain}.
 *
 * <p>Uses the Spring Security 6 lambda DSL (the older chained-method API is deprecated).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${baseframework.security.enabled:true}")
    private boolean securityEnabled;

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        if (securityEnabled) {
            http.authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        } else {
            http.authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        }

        return http.build();
    }
}
