package com.lovelyglam.oauthserver.config;

import java.util.UUID;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EntityScan(basePackages = "com.lovelyglam.database.model.entity")
@EnableJpaRepositories("com.lovelyglam.database.repository")
@ComponentScan(basePackages = {
    "com.lovelyglam.utils.config",
})
public class AuthorizationServerConfig {
    private final ClientRegisterDefaultConfig clientRegisterDefaultConfig;
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository (JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    ApplicationRunner clientRegisterRunner (RegisteredClientRepository registeredClientRepository) {
        return args -> {
            if (registeredClientRepository.findByClientId(clientRegisterDefaultConfig.getClientId()) == null) {
                registeredClientRepository.save(
                    RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(clientRegisterDefaultConfig.getClientId())
                    .clientSecret(clientRegisterDefaultConfig.getClientSecret())
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantTypes(agt -> agt.addAll(clientRegisterDefaultConfig.getAuthorizationGrantTypes()))
                    .redirectUris(uris -> uris.addAll(clientRegisterDefaultConfig.getRedirectUris()))
                    .scopes(scopes -> scopes.addAll(clientRegisterDefaultConfig.getScopes()))
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build());
            }
        };
    }
    
    @Bean 
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
        .build();
	}
}
