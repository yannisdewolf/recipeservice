package be.dewolf.recipes.recipeservice.controller.converter;

import be.dewolf.recipes.recipeservice.model.RecipeId;
import io.micrometer.observation.annotation.Observed;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeIdConverter implements Converter<String, RecipeId> {

    @Override
    public RecipeId convert(String source) {
        return RecipeId.from(source);
    }

}
