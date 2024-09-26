package com.lovelyglam.utils.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Value("${base-urls.front-end}")
    private String allowOrigin;
    @Value("${server.port}")
    private String port;
    @Value("${swagger-url}")
    private String swaggerUrl;

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @SuppressWarnings("null")
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                        .allowedOrigins(getCorsAllowed())
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH")
                        .exposedHeaders("*");
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(getCorsAllowed()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Accept",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Origin",
                "Authorization",
                "Content-Type",
                "Origin",
                "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(
                "Accept",
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Methods",
                "Access-Control-Allow-Origin",
                "Authorization",
                "Content-Type",
                "Origin",
                "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String[] getCorsAllowed() {
        List<String> corsAllowOrigins = new ArrayList<>();
        corsAllowOrigins.add("http://localhost:3000");
        if (!allowOrigin.equals("null"))
            corsAllowOrigins.add(allowOrigin);
        if (!swaggerUrl.equals("null")) 
            corsAllowOrigins.add(swaggerUrl);
        return corsAllowOrigins.toArray(new String[0]);
    }
}
