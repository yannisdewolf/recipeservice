package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.repository.MyRecipeRepository;
import be.dewolf.recipes.recipeservice.service.MessageSendingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;

// starts complete application but with a mocked RabbitMQ
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.data.inmemory=true",
                "spring.cloud.config.enabled=false"
        })
@ActiveProfiles(value = {"withoutrabbit", "integrationtest"})
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class RecipeserviceNoRabbitInMemoryDataApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MyRecipeRepository recipeRepository;

    @MockBean
    private MessageSendingService messageSendingService;

    @Test
    void savesRecipe() throws Exception {
        JSONObject createRecipeData = new JSONObject();
        createRecipeData.put("name", "soep");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> body = new HttpEntity<>(createRecipeData.toString(), headers);

        ResponseEntity<String> createResponse = testRestTemplate.postForEntity("/recipes", body, String.class);

        Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Map<String, Object> map = new ObjectMapper().readValue(createResponse.getBody(), Map.class);

        String id = (String) map.get("id");

        Recipe allFound = recipeRepository.getReferenceById(RecipeId.from(id));

        Assertions.assertThat(allFound.getName()).isEqualTo("soep");

        Mockito.verify(messageSendingService, Mockito.times(1))
                .sendMessage(argThat(r -> r.getName().equalsIgnoreCase("soep")));

    }

    @Test
    void getsRecipe() throws Exception {

        recipeRepository.save(Recipe.builder()
                .name("erwtensoep")
                .deleted(false)
                .id(RecipeId.from("2e01f6eb-212f-484b-8ed3-7f745ef132d7"))
                .build());

        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When
        ResponseEntity<String> forEntity = testRestTemplate.exchange("/recipes/2e01f6eb-212f-484b-8ed3-7f745ef132d7", HttpMethod.GET, entity, String.class);

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

    @Test
    void get_greeting() throws Exception {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.TEXT_PLAIN));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // When

        ResponseEntity<String> response = testRestTemplate.exchange("/recipes/welcome", HttpMethod.GET, entity, String.class);

        // Then
        Assertions.assertThat(response.getBody()).isEqualTo("hello world!");
    }

    @Test
    void delete_recipe() throws Exception {
        // Given
        RecipeId from = RecipeId.from("2e01f6eb-212f-484b-8ed3-7f745ef132d6");
        recipeRepository.save(Recipe.builder()
                .name("erwtensoep")
                .deleted(false)
                .id(from)
                .build());

        // When
        ResponseEntity<String> response = testRestTemplate.exchange("/recipes/2e01f6eb-212f-484b-8ed3-7f745ef132d6", HttpMethod.DELETE, null, String.class);

        // Then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() ->recipeRepository.getReferenceById(from))
                .withMessage("No recipe found with id RecipeId(uuid=2e01f6eb-212f-484b-8ed3-7f745ef132d6)");
    }

}
