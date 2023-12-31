package be.dewolf.recipes.recipeservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.StringJoiner;

@Table
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Ingredient implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "id", length = 36))
    })
    private IngredientId id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
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
