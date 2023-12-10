package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.MySqlTestContainerInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ContextConfiguration(initializers = {MySqlTestContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
@Sql(statements = {
        "DELETE from Recipe"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class AbstractDataTest {

    @Autowired
    private MyRecipeRepository recipeRepository;

//    @BeforeEach
//    public void cleanDatabase() {
//        log.info("cleaning");
//        recipeRepository.deleteAll();;
//    }

}
