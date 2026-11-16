package com.academia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class AsaasConfig {
    @Value("${ASAAS_API_KEY}")
    private String asaasApiKey;

    @Value("{ASAAS_API_URL")
    private String asaasApiUrl;

    @Bean
    public RestClient asaasPostClient(){
        return RestClient.builder()
                .baseUrl(asaasApiUrl)
                .defaultHeader("access_token",asaasApiKey)
                .defaultHeader("User-Agent","academia-portfolio")
                .build();
    }
}
