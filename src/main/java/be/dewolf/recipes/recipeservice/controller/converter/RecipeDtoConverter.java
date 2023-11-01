package be.dewolf.recipes.recipeservice.controller.converter;

import be.dewolf.recipes.recipeservice.controller.model.RecipeDto;
import be.dewolf.recipes.recipeservice.model.Recipe;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Component;

@Component
public class RecipeDtoConverter {

    public RecipeDto create(Recipe recipe) {
        return new RecipeDto(recipe.getId().getUuid(), recipe.getName());
    }

}
