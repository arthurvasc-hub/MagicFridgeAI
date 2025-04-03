package com.MagicFridgeAI.MagicFridgeAI.controller;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import com.MagicFridgeAI.MagicFridgeAI.service.ChatGptService;
import com.MagicFridgeAI.MagicFridgeAI.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class RecipeController {

    private final ChatGptService chatGptService;
    private FoodService service;

    public RecipeController(ChatGptService chatGptService, FoodService service) {
        this.chatGptService = chatGptService;
        this.service = service;
    }

    @GetMapping("/generate")
    public Mono<ResponseEntity<String>> generateRecipe(){
        List<FoodModel> foods = service.listOfFoods();
        return chatGptService.generateRecipe(foods)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
