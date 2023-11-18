package be.dewolf.recipes.recipeservice.config.rabbit;

import be.dewolf.recipes.recipeservice.events.Receiver;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(RecipeCreatedListenerProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.fullblownrabbit", havingValue = "true")
public class RecipeCreatedListenerConfig {

    @Bean
    Queue queue(RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        return QueueBuilder.durable(recipeCreatedListenerProperties.getQueueName())
//                .deadLetterExchange(recipeCreatedListenerProperties.getDlxName())
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
    @ConditionalOnProperty(value = "app.listener.active", havingValue = "true")
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    @ConditionalOnProperty(value = "app.listener.active", havingValue = "true")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter,
                                             RecipeCreatedListenerProperties recipeCreatedListenerProperties) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(recipeCreatedListenerProperties.getQueueName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

//    @Bean
//    Jackson2JsonMessageConverter messageConverter(ObjectMapper mapper) {
//        var converter = new Jackson2JsonMessageConverter(mapper);
//        converter.setCreateMessageIds(true); //create a unique message id for every message
//        return converter;
//    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory retryContainerFactory(
//            ConnectionFactory connectionFactory,
//            Jackson2JsonMessageConverter messageConverter,
//            MessageListenerAdapter listenerAdapter
//            ) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter);
//        return factory;
//    }

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


}
