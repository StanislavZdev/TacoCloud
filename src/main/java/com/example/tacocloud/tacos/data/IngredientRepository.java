package com.example.tacocloud.tacos.data;

import com.example.tacocloud.tacos.Ingredient;
import java.util.Optional;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();
    Optional<Ingredient> findById(String id);
    Ingredient save (Ingredient ingredient);
}
