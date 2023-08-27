package be.dewolf.recipes.recipeservice.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class IngredientId implements Serializable {

    private String uuid;

    public static IngredientId from(String id) {
        IngredientId ingredientId = new IngredientId();
        ingredientId.setUuid(id);
        return ingredientId;
    }

    public static IngredientId create() {
        return IngredientId.from(UUID.randomUUID().toString());
    }

}
