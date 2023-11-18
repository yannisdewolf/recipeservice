package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.model.Recipe;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
@Slf4j
public class DefaultMessageSendingService implements MessageSendingService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendMessage(Recipe recipe) {
        try {
            log.info("sending {}", recipe);
            rabbitTemplate.convertAndSend("recipeservicex", "recipe-created", objectMapper.writeValueAsString(recipe));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
