package com.yubit.microservices.composite.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.yubit")
public class ProductCompositeIntegrationConfig {
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
