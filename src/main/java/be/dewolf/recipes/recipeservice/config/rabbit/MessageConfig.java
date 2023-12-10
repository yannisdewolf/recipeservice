package be.dewolf.recipes.recipeservice.config.rabbit;

import be.dewolf.recipes.recipeservice.service.DefaultMessageSendingService;
import be.dewolf.recipes.recipeservice.service.MessageSendingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "app.fullblownrabbit", havingValue = "true")
public class MessageConfig {

    @Bean
    MessageSendingService messageSendingService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        return new DefaultMessageSendingService(rabbitTemplate, objectMapper);
    }

}
