package be.dewolf.recipes.recipeservice.config.jpa;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@Slf4j
@ConditionalOnProperty(value = "app.data.inmemory", havingValue = "false", matchIfMissing = true)
public class DataConfig {

    @PostConstruct
    public void afterSetup() {
        log.info("CREATED");
    }

}
