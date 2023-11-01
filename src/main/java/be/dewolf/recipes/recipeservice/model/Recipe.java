package be.dewolf.recipes.recipeservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Table
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe implements Serializable {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "uuid", column = @Column(name = "ID", length = 36))
    })
    private RecipeId id;

    private String name;
    @Column
    private String extraInfo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn
    private List<Ingredient> ingredients;

    @Column
    private boolean deleted;

    @Column
    private String url;

    @Version
    private Long version;

    @Override
    public String toString() {
        return new StringJoiner(", ", Recipe.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("extraInfo='" + extraInfo + "'")
//                .add("ingredients=[" + ingredients.stream().map(Ingredient::getId).map(IngredientId::getUuid).collect(Collectors.joining(",")) + "]")
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
