package be.dewolf.recipes.recipeservice.config.rabbit;

import be.dewolf.recipes.recipeservice.config.CustomErrorHandler;
import be.dewolf.recipes.recipeservice.events.Receiver;
import be.dewolf.recipes.recipeservice.service.DefaultMessageSendingService;
import be.dewolf.recipes.recipeservice.service.DummyMessageSendingService;
import be.dewolf.recipes.recipeservice.service.MessageSendingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;


@Configuration
@EnableConfigurationProperties(RecipeCreatedListenerProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.fullblownrabbit", havingValue = "true")
public class RecipeCreatedListenerConfig {

    @Bean
    Queue queue(RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return QueueBuilder.durable(recipeCreatedListenerProperties.getQueueName())
                .deadLetterExchange(recipeCreatedListenerProperties.getDlxName())
//                .deadLetterRoutingKey(recipeCreatedListenerProperties.getDlqName())
                .build();
    }

    @Bean
    Binding queueBinding(Queue queue, TopicExchange recipeExchange, RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return BindingBuilder
                .bind(queue)
                .to(recipeExchange)
                .with(recipeCreatedListenerProperties.getRoutingKey());
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter(ObjectMapper mapper) {
        var converter = new Jackson2JsonMessageConverter(mapper);
        converter.setCreateMessageIds(true); //create a unique message id for every message
        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory retryContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new CustomErrorHandler();
    }

    @Bean
    Queue dlq(RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return QueueBuilder.durable((recipeCreatedListenerProperties.getDlqName())).build();
    }

    @Bean
    FanoutExchange dlx(RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return new FanoutExchange(recipeCreatedListenerProperties.getDlxName());
    }

    @Bean
    Binding dlqBinding(Queue dlq,
                       FanoutExchange dlx,
                       RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return BindingBuilder
                .bind(dlq)
                .to(dlx);
    }

    @Bean
    MessageSendingService messageSendingService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        return new DefaultMessageSendingService(rabbitTemplate, objectMapper);
    }

}
