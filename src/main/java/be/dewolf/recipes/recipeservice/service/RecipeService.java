package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.RecipeConverter;
import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeService {

    private RecipeRepository recipeRepository;
    private RecipeConverter recipeConverter;

    public Recipe create(RecipeCreateData recipeCreateData) {
        return recipeRepository.findByName(recipeCreateData.name())
                .orElseGet(() -> recipeRepository.save(recipeConverter.create(recipeCreateData)));
    }

    public Recipe getRecipe(RecipeId recipeId) {
        return recipeRepository.getReferenceById(recipeId);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

}