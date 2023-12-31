package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeRepositoryIntegrationTest extends AbstractDataTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Sql(statements = {
            "INSERT INTO recipe(ID, name, deleted, created_on, last_modified_on) values ('2e01f6eb-212f-484b-8ed3-7f745ef132d7', 'erwtensoep', false, '2023-12-29 23:59:59', '2023-12-29 23:59:59')"
    })
    void getsByName() throws Exception {
        // Given
        String recipeName = "erwtensoep";

        // When
        Optional<Recipe> actual = recipeRepository.findByName(recipeName);

        // Then
        assertThat(actual).isPresent()
                .hasValueSatisfying(r -> {
                    assertThat(r.getName()).isEqualTo(recipeName);
                    assertThat(r.getCreatedOn()).isEqualTo(
                            LocalDateTime.of(2023, Month.DECEMBER, 29, 23, 59, 59).toInstant(ZoneOffset.UTC));
                });
    }

}