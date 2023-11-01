package be.dewolf.recipes.recipeservice.service;

import be.dewolf.recipes.recipeservice.model.Recipe;

public interface MessageSendingService {
    void sendMessage(Recipe recipe);
}
