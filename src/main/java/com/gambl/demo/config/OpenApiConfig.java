package com.gambl.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Currency Exchange API")
                        .version("1.0")
                        .description("""
                                This is an api that provides currency exchange rates,
                                which are updated every hour according to the currency names
                                """
                        ));
    }
}
