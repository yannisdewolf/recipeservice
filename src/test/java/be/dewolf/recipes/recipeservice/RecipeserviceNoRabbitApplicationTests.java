package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.repository.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "noRabbit")
public class RecipeserviceNoRabbitApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void contextLoads() throws Exception {
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


    }


}
