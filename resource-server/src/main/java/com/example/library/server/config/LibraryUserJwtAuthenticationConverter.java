package com.example.library.server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

/**
 * JWT converter that takes the roles from 'groups' claim of JWT token.
 */
public class LibraryUserJwtAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private static final String GROUPS_CLAIM = "groups";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String USERNAME_CLAIM = "preferred_username";

    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return Mono.just(extractAuthorities(jwt))
                .map((authorities) -> new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt)));

    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.getScopes(jwt).stream()
                .map(authority -> ROLE_PREFIX + authority.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        authorities.addAll(defaultGrantedAuthorities(jwt));

        return authorities;
    }

    private Collection<GrantedAuthority> defaultGrantedAuthorities(Jwt jwt) {
        return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt))
                .orElse(emptySet());
    }

    private String extractUsername(Jwt jwt) {
        return jwt.hasClaim(USERNAME_CLAIM) ? jwt.getClaimAsString(USERNAME_CLAIM) : jwt.getSubject();
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getScopes(Jwt jwt) {
        Object scopes = jwt.getClaims().get(GROUPS_CLAIM);
        if (scopes instanceof Collection) {
            return (Collection<String>) scopes;
        }

        return Collections.emptyList();
    }
}
