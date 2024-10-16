package com.lovelyglam.chatsocketserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lovelyglam.chatsocketserver.security.BusinessJwtAuthenticationFilter;
import com.lovelyglam.chatsocketserver.security.CustomerJwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class RestSecurityConfig {
    private final CorsConfig corsConfig;
    private final BusinessJwtAuthenticationFilter businessJwtAuthenticationFilter;
    private final CustomerJwtAuthenticationFilter customerJwtAuthenticationFilter;
    @Bean
    SecurityFilterChain authenticationFitterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .authorizeHttpRequests((auth) -> {
                auth.requestMatchers("/auth/**", "/pings/**", "/api-docs/**", "/swagger-ui/**", "/ws/**").permitAll();
                auth.anyRequest().authenticated();
            })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.addFilterBefore(this.customerJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(this.businessJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
