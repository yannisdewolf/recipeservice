package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, RecipeId>, JpaSpecificationExecutor<Recipe> {

    Optional<Recipe> findByName(String name);

}
