package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
@Slf4j
public class RecipeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeserviceApplication.class, args);
    }

    @Autowired
    private RecipeService recipeService;

    @Bean
    @ConditionalOnProperty(name = "app.initializedata", havingValue = "true" )
    @Order
    public ApplicationRunner init() {
        return (runner) -> {
            log.info("creating data");
            recipeService.create(new RecipeCreateData("Gevulde courgette"));
            recipeService.create(new RecipeCreateData("Lasagne met geroosterde paprika en kruidenkaas"));
        };
    }

}
