package com.lovelyglam.authserver.service.impl;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.lovelyglam.authserver.service.OAuth2TokenVerifierService;
import com.lovelyglam.database.model.constant.LoginMethodType;
import com.lovelyglam.database.model.dto.request.OAuth2AuthenticationRequest;
import com.lovelyglam.database.model.dto.request.OAuthAuthenticationRequest;
import com.lovelyglam.database.model.exception.AuthFailedException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;

@Service
public class OAuth2TokenVerifierServiceImpl implements OAuth2TokenVerifierService {
    @Value("${google.clientId}")
    private String googleClientId;
    public OAuth2AuthenticationRequest verifyToken (OAuthAuthenticationRequest request) {
        Optional<OAuth2AuthenticationRequest> authOptional = Optional.empty();
        switch (request.socialMethod()) {
            case FACEBOOK:
                authOptional = Optional.of(verifyFacebookToken(request.authToken()));
                break;
        
            case GOOGLE:
                authOptional = Optional.of(verityGoogleToken(request.authToken()));
                break;
        }
        
        return authOptional.orElseThrow(() -> new AuthFailedException("OAuth2 Failed Because Auth Token Is Not Valid"));
        
    }

    private OAuth2AuthenticationRequest verityGoogleToken (String googleToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
        .setAudience(Collections.singletonList(googleClientId))
        .build();
        GoogleIdToken token = null;
        try {
            token = verifier.verify(googleToken);
            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                var externalId = payload.getSubject();
                var externalMail = payload.getEmail();
                var avatar = (String) payload.get("picture");
                return OAuth2AuthenticationRequest.builder()
                    .socialMethod(LoginMethodType.GOOGLE)
                    .avatar(avatar)
                    .externalEmail(externalMail)
                    .externalId(externalId)
                .build();
            }
            throw new Exception();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AuthFailedException("OAuth2 Failed Because Auth Token Is Not Valid");
        }
    }

    private OAuth2AuthenticationRequest verifyFacebookToken (String facebookToken) {
        FacebookClient fbClient = new DefaultFacebookClient(facebookToken, Version.LATEST);
        try {
            var user = fbClient.fetchObject("me", User.class, Parameter.withFields("id,email,name,picture"));
            return OAuth2AuthenticationRequest.builder()
            .externalId(user.getId())
            .externalEmail(user.getEmail())
            .avatar(user.getPicture().getUrl())
            .socialMethod(LoginMethodType.FACEBOOK)
            .build();
        } catch (FacebookOAuthException ex) {
            throw new AuthFailedException("OAuth2 Failed Because Auth Token Is Not Valid");
        }
    }
}
