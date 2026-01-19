package com.github.appreciated.vortex_crud.demo.devplatform;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class GitSecurityConfig {

    @Bean
    @Order(1) // Ensure this runs before the standard Vaadin security chain
    public SecurityFilterChain gitSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/git/**")
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
