package com.lovelyglam.authserver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lovelyglam.authserver.security.BusinessJwtAuthenticationFilter;
import com.lovelyglam.authserver.security.CustomerJwtAuthenticationFilter;
import com.lovelyglam.authserver.security.GlamAuthenticationEntryPoint;
import com.lovelyglam.utils.config.CorsConfig;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Qualifier("glamAuthenticationEntryPoint")
    private final GlamAuthenticationEntryPoint glamAuthenticationEntryPoint;
    private final CorsConfig corsConfig;
    private final CustomerJwtAuthenticationFilter customerJwtAuthenticationFilter;
    private final BusinessJwtAuthenticationFilter businessJwtAuthenticationFilter;
    @Bean
    @Order(1)
    SecurityFilterChain authenticationFitterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((auth) -> 
                auth.requestMatchers("/auth/**", "/pings/**", "/api-docs/**", "/swagger-ui/**", "/registers/**", "/otp/**").permitAll().anyRequest().authenticated()
                )
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.authenticationEntryPoint(glamAuthenticationEntryPoint);
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
        http.addFilterBefore(this.customerJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(this.businessJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> {
            logout.logoutUrl("/auth/logout");
            logout.logoutSuccessHandler((request, response, authorization) -> SecurityContextHolder.clearContext());
        });
        return http.build();
    }
}
