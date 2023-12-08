package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecipeRepositoryDataJpaTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Sql(statements = {
            "INSERT INTO Recipe(ID, name, deleted) values ('2e01f6eb-212f-484b-8ed3-7f745ef132d7', 'erwtensoep', 0)"
    })
    void findByName() throws Exception {
        // Given
        Optional<Recipe> erwtensoep = recipeRepository.findByName("erwtensoep");
        // When

        // Then
        Assertions.assertThat(erwtensoep).hasValueSatisfying(r -> {
            r.getName().equalsIgnoreCase("erwtensoep1");
        });
    }

}