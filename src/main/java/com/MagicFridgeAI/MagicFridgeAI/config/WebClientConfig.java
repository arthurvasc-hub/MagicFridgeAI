package com.MagicFridgeAI.MagicFridgeAI.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@NoArgsConstructor
public class WebClientConfig {

    @Value("${chatgpt.api.url:https://api.openai.com/v1/chat/completions}")
    private String chatApiUrl;


    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(chatApiUrl).build();
    }
}
