package com.example.oidc.client.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain configure(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .matchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .matchers(EndpointRequest.to("health"))
                .permitAll()
                .matchers(EndpointRequest.to("info"))
                .permitAll()
                .pathMatchers("/api/admin")
                .hasAnyAuthority("SCOPE_library_admin", "ROLE_library_admin")
                .pathMatchers("/api/user")
                .hasAnyAuthority("SCOPE_library_user", "ROLE_library_user")
                .anyExchange()
                .authenticated()
                .and()
                .oauth2Login();

        return http.build();
    }
}
