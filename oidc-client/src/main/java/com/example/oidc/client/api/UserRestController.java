package com.example.oidc.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class UserRestController {

    @GetMapping("/api/user")
    public Mono<String> user() {
        return Mono.just("Only the user should be able to see this");
    }

    @GetMapping("/api/admin")
    public Mono<String> admin() {
        return Mono.just("Only the admin should be able to see this");
    }

    @GetMapping("/api/both")
    public Mono<String> both() {
        return Mono.just("Both admin and user should be able to see this");
    }

    @GetMapping("/userinfo")
    Mono<Map<String, Object>> userInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        return Mono.just(oauth2User.getAttributes());
    }
}
