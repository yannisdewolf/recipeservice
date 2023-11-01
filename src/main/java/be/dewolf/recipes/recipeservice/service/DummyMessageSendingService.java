package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.model.Recipe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMessageSendingService implements MessageSendingService {
    @Override
    public void sendMessage(Recipe recipe) {
        log.info("mock sending recipe.");
    }
}
