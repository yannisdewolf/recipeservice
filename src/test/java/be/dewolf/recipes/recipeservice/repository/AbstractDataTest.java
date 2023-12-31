package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.PostgresTestContainerInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = {"spring.cloud.config.enabled=false"})
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
@Sql(statements = {
        "DELETE from Recipe"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"integrationtest"})
public class AbstractDataTest {

    @Autowired
    private MyRecipeRepository recipeRepository;

}
