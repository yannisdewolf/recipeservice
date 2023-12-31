package be.dewolf.recipes.recipeservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.StringJoiner;

@Table
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Recipe implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "id", length = 36))
    })
    private RecipeId id;

    private String name;
    @Column
    private String extraInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "recipe")
    private List<Ingredient> ingredients;

    @Column
    private boolean deleted;

    @Column
    private String url;

    @Version
    private Long version;

    @Column
    @CreatedDate
    private Instant createdOn;

    @Column
    @LastModifiedDate
    private Instant lastModifiedOn;

    @Override
    public String toString() {
        return new StringJoiner(", ", Recipe.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("extraInfo='" + extraInfo + "'")
                .toString();
    }

    public void removeIngredient(String ingredientId) {
        ingredients.remove(ingredients.stream()
                .filter(ing -> ing.getId().getUuid().equals(ingredientId)).findFirst().get());

    }

    public void addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);

    }
}
