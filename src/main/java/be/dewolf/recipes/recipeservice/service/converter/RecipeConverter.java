package be.dewolf.recipes.recipeservice.service.converter;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.service.RecipeCreateData;
import org.springframework.stereotype.Component;

@Component
public class RecipeConverter {

    public Recipe create(RecipeCreateData recipeCreateData) {
        return Recipe.builder()
                .version(1L)
                .name(recipeCreateData.name())
                .id(RecipeId.create())
                .build();
    }

}
