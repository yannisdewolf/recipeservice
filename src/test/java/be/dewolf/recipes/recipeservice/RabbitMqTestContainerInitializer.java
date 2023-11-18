package be.dewolf.recipes.recipeservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public class RabbitMqTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.12.7-management-alpine").asCompatibleSubstituteFor("rabbitmq"));

    static {
        rabbitMQContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.rabbitmq.password=" + rabbitMQContainer.getAdminPassword(),
                "spring.rabbitmq.username=" + rabbitMQContainer.getAdminUsername(),
                "spring.rabbitmq.port=" + rabbitMQContainer.getAmqpPort()
        ).applyTo(applicationContext.getEnvironment());
    }
}
