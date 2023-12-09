package be.dewolf.recipes.recipeservice.controller;

import be.dewolf.recipes.recipeservice.controller.converter.RecipeDtoConverter;
import be.dewolf.recipes.recipeservice.controller.model.RecipeDto;
import be.dewolf.recipes.recipeservice.model.Recipe;
import be.dewolf.recipes.recipeservice.model.RecipeId;
import be.dewolf.recipes.recipeservice.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = RecipeController.class)
class RecipeControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeDtoConverter recipeDtoConverter;

    @Nested
    class GetByIdTests {

        @Test
        void returns_not_found_when_no_existing_recipe() throws Exception {
            // Given
            String recipeId = "1";

            when(recipeService.getRecipe(RecipeId.from(recipeId))).thenThrow(new EntityNotFoundException("not found"));

            // When
            ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/recipes/" + recipeId));

            // Then
            perform.andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void returns_found_recipe() throws Exception {
            // Given
            String recipeId = "1";

            Recipe foundRecipe = Recipe.builder()
                    .id(RecipeId.from(recipeId))
                    .name("spaghetti")
                    .version(1L)
                    .deleted(true)
                    .extraInfo("Spaghetti extra info")
                    .url("http://www.google.be")
                    .build();
            when(recipeService.getRecipe(RecipeId.from(recipeId))).thenReturn(foundRecipe);

            when(recipeDtoConverter.create(foundRecipe)).thenReturn(new RecipeDto("id-1", "the-name"));

            // When
            ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/recipes/" + recipeId));

            // Then
            perform.andExpect(MockMvcResultMatchers.status().isOk());
            perform.andExpect(content().contentType("application/json"))
                    .andExpect(content().json("""
                            {
                            "id": "id-1",
                            "name" : "the-name"
                            }"""));
        }

    }

    @Nested
    class CreateRecipeTests {

        @Test
        void createsRecipe() throws Exception {
            // Given
            String createRecipeRequest = """
                    {
                        "name": "spaghetti"
                    }""";

            Recipe recipe = Mockito.mock(Recipe.class);
            when(recipeService.create(argThat(recipeCreateData -> recipeCreateData.name().equals("spaghetti"))))
                    .thenReturn(recipe);
            when(recipeDtoConverter.create(recipe)).thenReturn(new RecipeDto("recipe-id", "spaghetti"));

            // When
            ResultActions createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                    .content(createRecipeRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE));

            // Then
            createResponse.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                            "id": "recipe-id",
                            "name": "spaghetti"
                            }""", true));
        }

    }

}