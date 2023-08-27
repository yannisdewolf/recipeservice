package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Ingredient;
import be.dewolf.recipes.recipeservice.model.IngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, IngredientId> {
}
