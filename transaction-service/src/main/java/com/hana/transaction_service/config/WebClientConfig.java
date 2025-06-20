package com.hana.transaction_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081/api/v1/accounts") // ⚠️ adapte l’URL si nécessaire
                .build();
    }


    @Bean
    public NewTopic transactionEventsTopic() {
        return new NewTopic("transaction-events", 1, (short) 1);
    }

}
