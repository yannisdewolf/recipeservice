package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MyRecipeRepository {

    Optional<Recipe> findByName(String name);

    Optional<Recipe> findById(RecipeId recipeId);

    Recipe save(Recipe recipe);

    List<Recipe> findAll();

    Recipe getReferenceById(RecipeId recipeId);

    void remove(RecipeId id);

}
