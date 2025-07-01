package com.cabbyai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CabbyAI API")
                        .version("1.0")
                        .description("REST API for CabbyAI Cab Booking System")
                        .contact(new Contact()
                                .name("CabbyAI Team")
                                .email("support@cabbyai.com")));
    }
}
