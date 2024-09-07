package com.lovelyglam.database.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "oauth2_registered_client")
public class Oauth2RegisteredClient {
    @Id
    @Column(name = "id", columnDefinition = "varchar(100)")
    private String id;
    @Column(name = "client_id", columnDefinition = "varchar(100)", nullable = false)
    private String clientId;
    @Column(name = "client_secret", columnDefinition = "varchar(200)", nullable = false)
    private String clientSecret;
    @Column(name = "client_id_issued_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Timestamp clientIdIssuedAt;
    @Column(name = "client_secret_expires_at", columnDefinition = "TIMESTAMP DEFAULT NULL", nullable = true)
    private Timestamp clientSecretExpiresAt;
    @Column(name = "client_name", columnDefinition = "varchar(200)", nullable = false)
    private String clientName;
    @Column(name = "client_authentication_methods", columnDefinition = "varchar(1000)", nullable = false)
    private String clientAuthenticationMethods;
    @Column(name = "authorization_grant_types", columnDefinition = "varchar(1000)", nullable = false)
    private String authorizationGrantTypes;
    @Column(name = "redirect_uris", columnDefinition = "varchar(1000) DEFAULT NULL")
    private String redirectUris;
    @Column(name = "post_logout_redirect_uris", columnDefinition = "varchar(1000) DEFAULT NULL")
    private String postLogoutRedirectUris;
    @Column(name = "scopes", columnDefinition = "varchar(1000)", nullable = false)
    private String scopes;
    @Column(name = "client_settings", columnDefinition = "varchar(2000)", nullable = false)
    private String clientSettings;
    @Column(name = "token_settings", columnDefinition = "varchar(2000)", nullable = false)
    private String tokenSettings;

}