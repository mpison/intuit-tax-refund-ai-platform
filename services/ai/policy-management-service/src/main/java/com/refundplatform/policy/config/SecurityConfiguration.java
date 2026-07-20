package com.refundplatform.policy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> extractRealmAuthorities(
                        jwt.getClaim("realm_access")
                )
        );

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(
                                        "/actuator/health/**",
                                        "/actuator/info"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/api/v1/admin/policies/**"
                                )
                                .hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(
                                jwt -> jwt.jwtAuthenticationConverter(
                                        converter
                                )
                        )
                );

        return http.build();
    }

    private Collection<GrantedAuthority> extractRealmAuthorities(
            Object realmAccessValue) {

        if (!(realmAccessValue instanceof Map<?, ?> realmAccess)) {
            return List.of();
        }

        Object rolesValue =
                realmAccess.get("roles");

        if (!(rolesValue instanceof Collection<?> roles)) {
            return List.of();
        }

        List<GrantedAuthority> authorities =
                new ArrayList<>();

        for (Object roleValue : roles) {
            String role =
                    String.valueOf(roleValue)
                            .trim()
                            .toUpperCase();

            if (!role.isEmpty()) {
                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        )
                );
            }
        }

        return authorities;
    }
}
