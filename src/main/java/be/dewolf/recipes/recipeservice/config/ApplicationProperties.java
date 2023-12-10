package be.dewolf.recipes.recipeservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.recipe")
public class ApplicationProperties {

    /*
    * A message to welcome users
    */
    String greeting;

}
