package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

class RecipeRepositoryIntegrationTest extends AbstractDataTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Sql(statements = {
            "INSERT INTO Recipe(ID, name, deleted) values ('2e01f6eb-212f-484b-8ed3-7f745ef132d7', 'erwtensoep', 0)"
    })
    void getsByName() throws Exception {
        // Given
        String recipeName = "erwtensoep";

        // When
        Optional<Recipe> actual = recipeRepository.findByName(recipeName);

        // Then
        Assertions.assertThat(actual).isPresent()
                        .map(Recipe::getName)
                                .hasValue(recipeName);
    }

}