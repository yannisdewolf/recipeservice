package be.dewolf.recipes.recipeservice.config.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperties rabbitProperties;

    // When no special characters as * or # are used in bindings,
    // a Topic Exchange behaves just like a Direct Exchange
    @Bean
    TopicExchange recipeExchange() {
        return new TopicExchange(rabbitProperties.getExchange(), true, false);
    }

}
