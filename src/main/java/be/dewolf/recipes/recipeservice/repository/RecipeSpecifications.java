package be.dewolf.recipes.recipeservice.repository;

import be.dewolf.recipes.recipeservice.model.Recipe;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class RecipeSpecifications {

    public static Specification<Recipe> withName(String recipeName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(be.dewolf.recipes.recipeservice.model.Recipe_.name), recipeName);
    }

}
