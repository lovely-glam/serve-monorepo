package com.lovelyglam.oauthserver.config;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class ClientRegisterDefaultConfig {
    @Value("${security.client.id}")
    private String clientId;
    @Value("${security.client.secret}")
    private String clientSecret;
    private Set<AuthorizationGrantType> authorizationGrantTypes;
    @Value("#{'${security.client.redirect-urls}'.split(',')}")
    private List<String> redirectUris;
    private Set<String> scopes;

    public ClientRegisterDefaultConfig() {
        this.authorizationGrantTypes = Set.of(AuthorizationGrantType.AUTHORIZATION_CODE, AuthorizationGrantType.REFRESH_TOKEN);
        this.scopes = Set.of(OidcScopes.OPENID,OidcScopes.PROFILE);
    }
}
