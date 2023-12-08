package be.dewolf.recipes.recipeservice.controller;

import be.dewolf.recipes.recipeservice.controller.converter.RecipeDtoConverter;
import be.dewolf.recipes.recipeservice.controller.model.CreateRecipeDto;
import be.dewolf.recipes.recipeservice.controller.model.RecipeDto;
import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.service.RecipeCreateData;
import be.dewolf.recipes.recipeservice.service.RecipeService;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
//@Observed(name = "my-recipe-controller")
@Slf4j
public class RecipeController {

    private RecipeService recipeService;
    private RecipeDtoConverter recipeDtoConverter;

    @GetMapping("/{id}")
    @Transactional
    public RecipeDto getById(@PathVariable RecipeId id) {
        log.info("get recipe by id {}", id);
        return recipeDtoConverter.create(recipeService.getRecipe(id));
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        log.info("get all recipes");
        return recipeService.findAll().stream().map(recipeDtoConverter::create).toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto createRecipe(@RequestBody CreateRecipeDto createRecipeDto) {
        Recipe recipe = recipeService.create(new RecipeCreateData(createRecipeDto.getName()));
        return recipeDtoConverter.create(recipe);
    }

}