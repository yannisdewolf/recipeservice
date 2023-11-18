package be.dewolf.recipes.recipeservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("arm64v8/mysql:oracle").asCompatibleSubstituteFor("mysql"));

    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.12.7-management-alpine").asCompatibleSubstituteFor("rabbitmq"))
            ;

    static {
        rabbitMQContainer.start();
        mySQLContainer.start();
//        Startables.deepStart(
//                mySQLContainer,
//                rabbitMQContainer);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                "spring.datasource.driverClassName=" + mySQLContainer.getDriverClassName(),
                "spring.datasource.username=" + mySQLContainer.getUsername(),
                "spring.datasource.password=" + mySQLContainer.getPassword(),
                "spring.rabbitmq.password=" + rabbitMQContainer.getAdminPassword(),
                "spring.rabbitmq.username=" + rabbitMQContainer.getAdminUsername(),
                "spring.rabbitmq.port=" + rabbitMQContainer.getAmqpPort()
        ).applyTo(applicationContext.getEnvironment());
    }
}
