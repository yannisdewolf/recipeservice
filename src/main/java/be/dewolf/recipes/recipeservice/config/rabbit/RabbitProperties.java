package be.dewolf.recipes.recipeservice.config.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbit")
@Data
public class RabbitProperties {

    String exchange;

}
