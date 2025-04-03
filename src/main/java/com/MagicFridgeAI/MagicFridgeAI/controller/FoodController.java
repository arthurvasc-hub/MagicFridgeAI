package com.MagicFridgeAI.MagicFridgeAI.controller;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import com.MagicFridgeAI.MagicFridgeAI.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/food")
@CrossOrigin(origins = "*")
public class FoodController {


    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<FoodModel>> findById(@PathVariable Long id){
        Optional<FoodModel> foodById = foodService.buscarFoodPorId(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(foodById);
    }

    //GET
    @GetMapping
    public ResponseEntity<List<FoodModel>> allFoods(){
        List<FoodModel> foods = foodService.listOfFoods();
        return ResponseEntity.ok(foods);
    }



    @PostMapping
    public ResponseEntity<FoodModel> create (@RequestBody FoodModel foodModel){
        FoodModel foodSaved = foodService.save(foodModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodSaved);
    }

    //UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<Optional<FoodModel>> updateFood(@PathVariable Long id, @RequestBody FoodModel newFood){
        Optional<FoodModel> updatedFood = foodService.updateFood(id, newFood);
        if(updatedFood.isPresent()){
            return ResponseEntity.ok(updatedFood);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.empty());
        }
    }

    //DELETE
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable Long id){
        foodService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Alimento com o id: " + "'" + id +"'" + " deletado com sucesso.");
    }
}
