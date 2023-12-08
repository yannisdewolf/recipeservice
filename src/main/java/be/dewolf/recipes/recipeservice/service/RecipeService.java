package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.service.converter.RecipeConverter;
import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeService {

    private RecipeRepository recipeRepository;
    private RecipeConverter recipeConverter;
    private MessageSendingService messageSendingService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Recipe create(RecipeCreateData recipeCreateData) {
        Optional<Recipe> foundRecipe = recipeRepository.findByName(recipeCreateData.name());
        if (foundRecipe.isPresent()) {
            log.debug("Recipe found with name {}", recipeCreateData.name());
        }
        return foundRecipe
                .orElseGet(() -> {
                    return createRecipe(recipeCreateData);
                });
    }

    private Recipe createRecipe(RecipeCreateData recipeCreateData) {
        Recipe recipe = recipeConverter.create(recipeCreateData);
        log.debug("Recipe {} created", recipe);
        Recipe createdRecipe = recipeRepository.save(recipe);
        messageSendingService.sendMessage(createdRecipe);
        return createdRecipe;
    }

    public Recipe getRecipe(RecipeId recipeId) {
        return recipeRepository.getReferenceById(recipeId);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

}