package com.MagicFridgeAI.MagicFridgeAI.controller;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import com.MagicFridgeAI.MagicFridgeAI.service.ChatGptService;
import com.MagicFridgeAI.MagicFridgeAI.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
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
        if (foods.isEmpty()) {
            return Mono.just(ResponseEntity.ok("Adicione alguns alimentos à geladeira primeiro!"));
        }
        return chatGptService.generateRecipe(foods)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.internalServerError().body("Não foi possível gerar a receita."));
    }
}
