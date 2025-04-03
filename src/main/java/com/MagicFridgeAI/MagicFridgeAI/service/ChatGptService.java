package com.MagicFridgeAI.MagicFridgeAI.service;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatGptService {

    private final String apiKey = System.getenv("API_KEY");
    private final WebClient webClient;

    public ChatGptService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe(List<FoodModel> foods) {

        String alimentos = foods.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, Validade: %s",
                                item.getName(), item.getCategory(), item.getQuantity(), item.getValidity()))
                .collect(Collectors.joining("\n"));

        String prompt = "Baseado no meu banco de dados me sugira uma receita com os seguintes itens: " + alimentos;

        Map<String, Object> body = Map.of(
                "model", "gpt-4o",
                "messages", List.of(
                        Map.of("role", "developer", "content", prompt)
                )
        );

        return webClient.post()
                .uri("https://api.openai.com/v1/chat/completions") // URL correta
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class) // Desserializa a resposta JSON para um Map
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString();
                    }
                    return "Nenhuma receita foi gerada.";
                });
    }
}

