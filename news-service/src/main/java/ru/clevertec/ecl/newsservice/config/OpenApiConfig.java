package ru.clevertec.ecl.newsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String TITLE = "News Management System";
    private static final String DESCRIPTION = "RESTful web-service that implements functionality " +
            "for workingwith a news management system";
    private static final String VERSION = "0.0.1-SNAPSHOT";

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION));
    }
}
