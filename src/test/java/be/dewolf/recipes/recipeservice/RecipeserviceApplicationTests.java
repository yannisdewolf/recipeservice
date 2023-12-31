package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.repository.MyRecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "app.initializedata=false",
                "app.listener.active=false",
                "app.fullblownrabbit=true",
//                "app.data.inmemory=false",
                "spring.cloud.config.enabled=false"
        })
@ActiveProfiles(value = {"integrationtest"})
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class, RabbitMqTestContainerInitializer.class})
class RecipeserviceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MyRecipeRepository recipeRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {

    }

    @Test
    void sendMessageWhenCreatingRecipe() throws Exception {
        JSONObject createRecipeData = new JSONObject();
        createRecipeData.put("name", "soep");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> body = new HttpEntity<>(createRecipeData.toString(), headers);

        ResponseEntity<String> createResponse = testRestTemplate.postForEntity("/recipes", body, String.class);

        Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Recipe> allFound = recipeRepository.findAll();

        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(allFound).hasSize(1)
                    .extracting(Recipe::getName)
                    .contains("soep");
            assertions.assertThat(allFound.get(0)).extracting(Recipe::getCreatedOn).isNotNull();
        });

        await().atMost(5, TimeUnit.SECONDS)
                .until(() -> {
                    Integer messageCount = rabbitTemplate.execute(channel -> channel.queueDeclare("recipeservice.recipe-created", true, false, false, null).getMessageCount());
                    String s = (String) rabbitTemplate.receiveAndConvert("recipeservice.recipe-created");
                    System.out.println(s);
                    return messageCount == 1 && s != null;
                });

    }

    @Slf4j
    @TestConfiguration
    static class RabbitTestConfiguration {

        @Bean
        DLQListener dlqListener() {
            return new DLQListener();
        }

    }

}
