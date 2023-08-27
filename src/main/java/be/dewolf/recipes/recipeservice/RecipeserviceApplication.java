package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import be.dewolf.recipes.recipeservice.service.RecipeCreateData;
import be.dewolf.recipes.recipeservice.service.RecipeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class RecipeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeserviceApplication.class, args);
    }

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Bean
    public ApplicationRunner init() {
        return (runner) -> {

            recipeService.create(new RecipeCreateData("Gevulde courgette"));
            recipeService.create(new RecipeCreateData("Lasagne met geroosterde paprika en kruidenkaas"));
        };
    }


    @RestController
    @RequestMapping("/recipes")
    @AllArgsConstructor
    class RecipeController {

        private RecipeService recipeService;

        @GetMapping("/{id}")
        public Recipe getById(RecipeId id) {
            return recipeService.getRecipe(id);
        }

        @GetMapping
        public List<Recipe> getAll() {
            return recipeService.findAll();
        }

    }

}
