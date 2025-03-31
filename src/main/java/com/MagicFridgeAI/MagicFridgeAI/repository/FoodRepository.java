package com.MagicFridgeAI.MagicFridgeAI.repository;

import com.MagicFridgeAI.MagicFridgeAI.model.FoodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodModel, Long> {
}
