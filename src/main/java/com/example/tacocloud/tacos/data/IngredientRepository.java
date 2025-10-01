package com.example.tacocloud.tacos.data;

import com.example.tacocloud.tacos.Ingredient;
import org.springframework.data.repository.CrudRepository;


public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {

}
