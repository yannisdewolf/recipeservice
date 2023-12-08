package be.dewolf.recipes.recipeservice.config;

import be.dewolf.recipes.recipeservice.service.RecipeCreateData;
import be.dewolf.recipes.recipeservice.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "app.initializedata", havingValue = "true")
public class InitialLoadConfig {

    @Bean
    public ApplicationRunner init(RecipeService recipeService) {
        return (runner) -> {
            log.info("creating data");
            recipeService.create(new RecipeCreateData("Gevulde courgette"));
            recipeService.create(new RecipeCreateData("Lasagne met geroosterde paprika en kruidenkaas"));
        };
    }


}
