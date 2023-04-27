package com.example.library.server.config;

import com.example.library.server.common.Role;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf()
                .disable()
                .authorizeExchange()
                .matchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .matchers(EndpointRequest.to("health"))
                .permitAll()
                .matchers(EndpointRequest.to("info"))
                .permitAll()
                .matchers(EndpointRequest.toAnyEndpoint())
                .hasRole(Role.LIBRARY_ADMIN.name())
                .pathMatchers(HttpMethod.POST, "/books/{bookId}/borrow")
                .hasRole(Role.LIBRARY_USER.name())
                .pathMatchers(HttpMethod.POST, "/books/{bookId}/return")
                .hasRole(Role.LIBRARY_USER.name())
                .pathMatchers(HttpMethod.POST, "/books")
                .hasRole(Role.LIBRARY_CURATOR.name())
                .pathMatchers(HttpMethod.DELETE, "/books")
                .hasRole(Role.LIBRARY_CURATOR.name())
                .pathMatchers("/users/**")
                .hasRole(Role.LIBRARY_ADMIN.name())
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(libraryUserJwtAuthenticationConverter());
        return http.build();
    }

    @Bean
    public LibraryUserJwtAuthenticationConverter libraryUserJwtAuthenticationConverter() {
        return new LibraryUserJwtAuthenticationConverter();
    }
}
