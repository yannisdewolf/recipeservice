package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.RecipeConverter;
import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
//@Observed(name = "recipe-domain-service")
public class RecipeService {

    private RecipeRepository recipeRepository;
    private RecipeConverter recipeConverter;

    private MessageSendingService messageSendingService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Recipe create(RecipeCreateData recipeCreateData) {
        return recipeRepository.findByName(recipeCreateData.name())
                .orElseGet(() -> createRepository(recipeCreateData));
    }

    private Recipe createRepository(RecipeCreateData recipeCreateData) {
        Recipe r = recipeRepository.save(recipeConverter.create(recipeCreateData));
        messageSendingService.sendMessage(r);
        return r;
    }

    public Recipe getRecipe(RecipeId recipeId) {
        return recipeRepository.getReferenceById(recipeId);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

}