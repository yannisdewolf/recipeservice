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
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "withoutrabbit")
@Testcontainers
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class RecipeserviceNoRabbitApplicationTests {

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("arm64v8/mysql:oracle").asCompatibleSubstituteFor("mysql"));

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @MockBean
    private MessageSendingService messageSendingService;

    @Value("${app.fullblownrabbit}")
    private String fullblownRabbit;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
    }

    @Test
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
            "INSERT INTO Recipe(ID, name, deleted) value ('2e01f6eb-212f-484b-8ed3-7f745ef132d7', 'erwtensoep', 0)"
    })
    void getsRecipe() throws Exception {
        // Given

        // When
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity("/recipes/2e01f6eb-212f-484b-8ed3-7f745ef132d7", String.class);

        // Then
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = forEntity.getBody();
        JSONAssert.assertEquals("{\"id\":\"2e01f6eb-212f-484b-8ed3-7f745ef132d7\",\"name\":\"erwtensoep\"}", body, true);
    }

}
