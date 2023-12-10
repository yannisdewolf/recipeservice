package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;

import java.util.List;
import java.util.Optional;

public interface MyRecipeRepository {

    Optional<Recipe> findByName(String name);

    Recipe save(Recipe recipe);

    List<Recipe> findAll();

    Recipe getReferenceById(RecipeId recipeId);

}
