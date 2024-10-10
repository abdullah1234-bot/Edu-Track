package com.fifth_semester.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                new Info().title("Edu Track APIs")
                        .description("Student Management Project  \nBy Abdullah Kapadia")
                )
                .servers(List.of(new Server().url("http://localhost:8081").description("local"),
                        new Server().url("http://localhost:8082").description("live")))
                .tags(
                        List.of(
                                new Tag().name("Authentication APIs")
                                        .name("Student Profile Management APIs")
                        )
                )
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
