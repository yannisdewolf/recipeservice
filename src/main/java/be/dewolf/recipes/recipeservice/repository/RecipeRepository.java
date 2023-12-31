package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, RecipeId>, JpaSpecificationExecutor<Recipe>, MyRecipeRepository {

    Optional<Recipe> findByName(String name);

    @Modifying
    @Transactional
    @Query("update Recipe r set r.deleted = true where r.id= :id")
    void remove(RecipeId id);

    @Override
    @Query("select r from Recipe r where r.id = :recipeId and r.deleted = false")
    Optional<Recipe> findById(RecipeId recipeId);
}
