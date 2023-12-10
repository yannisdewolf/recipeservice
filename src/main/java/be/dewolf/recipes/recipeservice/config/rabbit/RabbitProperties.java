package be.dewolf.recipes.recipeservice.config.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.rabbit")
public class RabbitProperties {

    String exchange;

}
