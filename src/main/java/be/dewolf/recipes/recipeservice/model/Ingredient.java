package be.dewolf.recipes.recipeservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.StringJoiner;

@Table
@Entity
@Data
@NoArgsConstructor
public class Ingredient implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "ID", length = 36))
    })
    private IngredientId id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Override
    public String toString() {
        return new StringJoiner(", ", Ingredient.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("recipe=" + recipe.getId())
                .toString();
    }
}
