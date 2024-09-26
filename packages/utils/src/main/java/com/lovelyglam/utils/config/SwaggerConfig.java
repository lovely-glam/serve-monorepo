package com.lovelyglam.utils.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    @Value("${base-urls.api}")
    private String currentDomain;
    @Value("${spring.profiles.active}")
    private String envString;
    @Value("${server.port}")
    private String port;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private static final String SECURITY_STRING_NAME = "Bearer Auth";
    @Bean
    public OpenAPI openAPI() {
        
        List<Server> serverList = new ArrayList<>();
        Server server = new Server();
        server.setUrl(String.format("http://localhost:%s%s", port, contextPath));
        server.setDescription(String.format("Server URL in %s environment", envString));
        serverList.add(server);
        if(!currentDomain.equals("null")){
            server = new Server();
            server.setUrl(String.format("https://%s%s", currentDomain, contextPath));
            server.setDescription(String.format("Server URL in %s environment", envString));
            serverList.add(server);
        }
        Contact contact = new Contact();
        contact.setEmail("contact-me@lovelyglam.com");
        contact.setName("EXE2");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("LOVELY GLAM API ENDPOINT")
            .version("1.0")
            .contact(contact)
            .description("REST API ENDPOINT USING SPRING BOOT FOR LOVELY GLAM").termsOfService("https://github.com")
            .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(serverList)
                .addSecurityItem(new SecurityRequirement()
                .addList(SECURITY_STRING_NAME))
                .components(new Components()
                .addSecuritySchemes(SECURITY_STRING_NAME, new SecurityScheme()
                .name(SECURITY_STRING_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
    }
}
