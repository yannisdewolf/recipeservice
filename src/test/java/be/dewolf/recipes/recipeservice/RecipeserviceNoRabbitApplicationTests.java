package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import be.dewolf.recipes.recipeservice.service.MessageSendingService;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

// starts complete application but with a mocked RabbitMQ
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "withoutrabbit")
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@ContextConfiguration(initializers = {MySqlTestContainerInitializer.class})

public class RecipeserviceNoRabbitApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @MockBean
    private MessageSendingService messageSendingService;

    @Test
    @Sql(statements = {
            "DELETE from Recipe"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void savesRecipe() throws Exception {
        JSONObject createRecipeData = new JSONObject();
        createRecipeData.put("name", "soep");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> body = new HttpEntity<>(createRecipeData.toString(), headers);

        ResponseEntity<String> createResponse = testRestTemplate.postForEntity("/recipes", body, String.class);

        Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Recipe> allFound = recipeRepository.findAll();

        Assertions.assertThat(allFound).hasSize(1)
                .extracting(Recipe::getName)
                .contains("soep");

        Mockito.verify(messageSendingService, Mockito.times(1))
                .sendMessage(argThat(r -> r.getName().equalsIgnoreCase("soep")));

    }

    @Test
    @Sql(statements = {
            "INSERT INTO Recipe(ID, name, deleted) values ('2e01f6eb-212f-484b-8ed3-7f745ef132d7', 'erwtensoep', 0)"
    })
    @Sql(statements = {
            "DELETE from Recipe"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getsRecipe() throws Exception {
        // Given

        // When
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity("/recipes/2e01f6eb-212f-484b-8ed3-7f745ef132d7", String.class);

        // Then
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = forEntity.getBody();
        JSONAssert.assertEquals("{\"id\":\"2e01f6eb-212f-484b-8ed3-7f745ef132d7\",\"name\":\"erwtensoep\"}", body, true);
    }

    @Test
    void returns_404_when_not_found() throws Exception {
        // Given

        // When
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity("/recipes/2e01f6eb-212f-484b-8ed3-7f745ef132d7", String.class);

        // Then
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
