package com.lovelyglam.authserver.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lovelyglam.authserver.service.impl.BusinessUserDetailService;
import com.lovelyglam.authserver.service.impl.LocalUserDetailService;
import com.lovelyglam.authserver.service.impl.SystemUserDetailService;
import com.lovelyglam.database.model.exception.AuthFailedException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServerAuthenticationProvider implements AuthenticationProvider {
    private final BusinessUserDetailService businessUserDetailService;
    private final LocalUserDetailService localUserDetailService;
    private final SystemUserDetailService systemUserDetailService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = null;
        
        if (username.startsWith("customer_")) {
            var originalUsername = username.substring("customer_".length());
            userDetails = localUserDetailService.loadUserByUsername(originalUsername);
        } else if (username.startsWith("nail_")) {
            var originalUsername = username.substring("nail_".length());
            userDetails = businessUserDetailService.loadUserByUsername(originalUsername);
        } else if (username.startsWith("system_")) {
            var originalUsername = username.substring("system_".length());
            userDetails = systemUserDetailService.loadUserByUsername(originalUsername);
        }
        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), userDetails.getAuthorities());
        }
        throw new AuthFailedException("Authentication Failed");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
}
