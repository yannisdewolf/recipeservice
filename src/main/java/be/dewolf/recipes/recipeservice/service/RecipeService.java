package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.MyRecipeRepository;
import be.dewolf.recipes.recipeservice.service.converter.RecipeConverter;
import jakarta.persistence.EntityNotFoundException;
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

    private MyRecipeRepository recipeRepository;
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
        return recipeRepository.findById(recipeId).orElseThrow(() -> new EntityNotFoundException("No recipe found with id " + recipeId));
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll().stream().filter(r -> !r.isDeleted()).toList();
    }

    @Transactional
    public void delete(RecipeId recipeId) {
        recipeRepository.findById(recipeId).ifPresent(r -> {
            r.setDeleted(true);
        });

    }
}