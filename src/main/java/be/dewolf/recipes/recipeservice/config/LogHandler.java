package be.dewolf.recipes.recipeservice.config;

import be.dewolf.recipes.recipeservice.controller.RecipeController;
import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;

// Example of plugging in a custom handler that in this case will print a statement before and after all observations take place
@Component
@Slf4j
class LogHandler implements ObservationHandler<Observation.Context> {


    @Override
    public void onStart(Observation.Context context) {
        log.info("Before running the observation for context [{}], recipe [{}]", context.getName(), getUserTypeFromContext(context));
        context.getHighCardinalityKeyValues().stream().map(KeyValue::getValue).forEach(log::info);
    }

    @Override
    public void onStop(Observation.Context context) {
        log.info("After running the observation for context [{}], recipe [{}]", context.getName(), getUserTypeFromContext(context));
        context.getHighCardinalityKeyValues().stream().map(KeyValue::getValue).forEach(log::info);
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return ofNullable(context.getName()).map(n -> n.equalsIgnoreCase("my-recipe-controller")).orElse(false);
    }

    private String getUserTypeFromContext(Observation.Context context) {
        return StreamSupport.stream(context.getLowCardinalityKeyValues().spliterator(), false)
                .filter(keyValue -> "recipe".equals(keyValue.getKey()))
                .map(KeyValue::getValue)
                .findFirst()
                .orElse("UNKNOWN");
    }
}