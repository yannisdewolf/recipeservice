package be.dewolf.recipes.recipeservice;

import be.dewolf.recipes.recipeservice.config.ApplicationProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class RecipeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeserviceApplication.class, args);
    }

}
