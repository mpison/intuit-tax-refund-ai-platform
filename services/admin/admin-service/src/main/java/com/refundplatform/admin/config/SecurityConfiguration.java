package com.refundplatform.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
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

        http
                .cors(
                        cors -> {
                        }
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                )
                .authorizeHttpRequests(
                        requests ->
                                requests
                                        .requestMatchers(
                                                "/actuator/health/**",
                                                "/actuator/info"
                                        )
                                        .permitAll()
                                        .requestMatchers(
                                                "/api/v1/admin/**"
                                        )
                                        .hasRole(
                                                "ADMIN"
                                        )
                                        .anyRequest()
                                        .authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 ->
                                oauth2.jwt(
                                        jwt ->
                                                jwt.jwtAuthenticationConverter(
                                                        jwtAuthenticationConverter()
                                                )
                                )
                );

        return http.build();
    }

    private Converter<
            Jwt,
            ? extends AbstractAuthenticationToken
            > jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                this::extractRealmAuthorities
        );

        return converter;
    }

    private Collection<GrantedAuthority> extractRealmAuthorities(
            Jwt jwt) {

        Object realmAccessObject =
                jwt.getClaim(
                        "realm_access"
                );

        if (
                !(realmAccessObject instanceof Map<?, ?> realmAccess)
        ) {

            return List.of();
        }

        Object rolesObject =
                realmAccess.get(
                        "roles"
                );

        if (
                !(rolesObject instanceof Collection<?> roles)
        ) {

            return List.of();
        }

        List<GrantedAuthority> authorities =
                new ArrayList<>();

        for (
            Object roleObject
            : roles
        ) {

            String roleName =
                    String.valueOf(
                            roleObject
                    )
                    .trim();

            if (
                    !roleName.isEmpty()
            ) {

                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_"
                                        + roleName
                                        .toUpperCase()
                        )
                );
            }
        }

        return authorities;
    }
}
