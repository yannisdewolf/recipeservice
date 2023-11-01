package be.dewolf.recipes.recipeservice.config.rabbit;

import be.dewolf.recipes.recipeservice.service.DummyMessageSendingService;
import be.dewolf.recipes.recipeservice.service.MessageSendingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "app.fullblownrabbit", havingValue = "false")
public class DummyRabbitConfig {

    @Bean
    MessageSendingService dummyMessageSendingService() {
        return new DummyMessageSendingService();
    }

}
