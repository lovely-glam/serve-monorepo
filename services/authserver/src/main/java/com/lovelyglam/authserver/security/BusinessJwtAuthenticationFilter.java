package com.lovelyglam.authserver.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lovelyglam.authserver.service.JwtService;
import com.lovelyglam.authserver.service.impl.BusinessUserDetailService;
import com.lovelyglam.database.model.constant.TokenType;
import com.lovelyglam.database.model.other.UserClaims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BusinessJwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final BusinessUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String jwt = jwtService.getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt, TokenType.ACCESS_TOKEN)) {
                UserClaims username = jwtService.getUserClaimsFromJwt(jwt, TokenType.ACCESS_TOKEN);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
