package com.lovelyglam.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lovelyglam.authserver.security.JwtAuthenticationFilter;
import com.lovelyglam.authserver.service.impl.OAuthUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthUserDetailService oAuthUserDetailService;
    @Bean
    @Order(1)
    SecurityFilterChain authenticationFitterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((auth) -> 
                auth.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
        http.oauth2Login((oauth2) -> {
            oauth2.defaultSuccessUrl("/auth/oauth2", true);
            oauth2.failureUrl("/auth/oauth2/failed");
            oauth2.userInfoEndpoint((oauth2Info) -> {
                oauth2Info.userService(oAuthUserDetailService);
            });
        });
        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> {
            logout.logoutUrl("/auth/logout");
            logout.logoutSuccessHandler((request, response, authorization) -> SecurityContextHolder.clearContext());
        });
        return http.build();
    }
}
