package com.init.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.springframework.security.config.Elements.JWT;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private final Environment environment;

    @Bean
    public OpenAPI openAPI() {
        String[] profiles = environment.getActiveProfiles();
        String activeProfile = (profiles.length > 0) ? profiles[0] : "default";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);

        return new OpenAPI()
                .info(apiInfo(activeProfile))
                .addSecurityItem(securityRequirement)
                .components(securitySchemes());
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    private Info apiInfo(String activeProfile) {
        return new Info()
                .title("{project-name} API (" + activeProfile + ")")
                .description("{project-desc}")
                .version("v1.0.0");
    }

    private Components securitySchemes() {
        final var securitySchemeAccessToken = new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new Components()
                .addSecuritySchemes(JWT, securitySchemeAccessToken);
    }
}
