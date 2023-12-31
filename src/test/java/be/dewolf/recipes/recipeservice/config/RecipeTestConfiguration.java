package be.dewolf.recipes.recipeservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class RecipeTestConfiguration {


    @Bean
    @Primary
    AuditorAwareImpl auditorProvider() {
        return new TestAuditorAware();
    }

    public static class TestAuditorAware extends AuditorAwareImpl {

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("test");
        }
    }

    public static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("Default auditor");
        }
    }

}
