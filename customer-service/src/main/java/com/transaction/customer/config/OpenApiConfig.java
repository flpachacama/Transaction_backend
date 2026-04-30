package com.transaction.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transactionOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Customer Service API")
                        .version("1.0.0")
                        .description("API para gestión de clientes, login básico JWT y publicación de eventos RabbitMQ"));
    }
}
