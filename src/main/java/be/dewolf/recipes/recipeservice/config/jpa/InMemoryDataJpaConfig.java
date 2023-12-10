package be.dewolf.recipes.recipeservice.config.jpa;


import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.MyRecipeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ConditionalOnProperty(value = "app.data.inmemory", havingValue = "true")
public class InMemoryDataJpaConfig {

    @PostConstruct
    public void inited() {
        System.out.println();
    }

    @Bean
    public MyRecipeRepository myRecipeRepository() {
        return new MyRecipeRepository() {

            private final List<Recipe> recipes = new ArrayList<>();

            @Override
            public Optional<Recipe> findByName(String name) {
                return recipes.stream().filter(r -> r.getName().equals(name)).findFirst();
            }

            @Override
            public Recipe save(Recipe recipe) {
                try {
                    Recipe existing = getReferenceById(recipe.getId());
                    existing.setDeleted(recipe.isDeleted());
                    existing.setUrl(recipe.getUrl());
                    existing.setVersion(recipe.getVersion());
                    existing.setExtraInfo(recipe.getExtraInfo());
                    return existing;
                } catch (EntityNotFoundException e) {
                    recipes.add(recipe);
                    return recipe;
                }
            }

            @Override
            public List<Recipe> findAll() {
                return new ArrayList<>(recipes);
            }

            @Override
            public Recipe getReferenceById(RecipeId recipeId) {
                return recipes.stream().filter(r -> r.getId().equals(recipeId)).findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No recipe found with id " + recipeId));
            }
        };
    }

}
