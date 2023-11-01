package be.dewolf.recipes.recipeservice.config.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.rabbit.recipe-created-listener")
public class RecipeCreatedListenerProperties {

    private String queueName;
    private String routingKey; // which routing key to listen to
    private String dlxName;

    public String getDlqName() {
        return queueName + ".dlq";
    }

}
