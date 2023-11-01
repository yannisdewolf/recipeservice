package be.dewolf.recipes.recipeservice.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RecipeId implements Serializable {

    private String uuid;

    public static RecipeId from(String id) {
        RecipeId recipeId = new RecipeId();
        recipeId.setUuid(id);
        return recipeId;
    }

    public static RecipeId create() {
        RecipeId recipeId = new RecipeId();
        recipeId.setUuid(UUID.randomUUID().toString());
        return recipeId;
    }


}
