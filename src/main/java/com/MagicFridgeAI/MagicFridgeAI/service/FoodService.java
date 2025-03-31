package com.MagicFridgeAI.MagicFridgeAI.service;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import com.MagicFridgeAI.MagicFridgeAI.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {


    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public FoodModel save(FoodModel food){
        return foodRepository.save(food);
    }


    public List<FoodModel> listOfFoods(){
        return foodRepository.findAll();
    }

    public void deleteById(Long id){
        foodRepository.deleteById(id);
    }

    public Optional<FoodModel> updateFood(Long id, FoodModel food){
        Optional<FoodModel> foodById = foodRepository.findById(id);
        if(foodById.isPresent()){
            food.setId(id);
            FoodModel newFood = foodRepository.save(food);
            return Optional.of(newFood);
        } else {
            return Optional.empty();
        }
    }

    public Optional<FoodModel> buscarFoodPorId(Long id){
        return foodRepository.findById(id);
    }
}
