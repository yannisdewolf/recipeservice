package be.dewolf.recipes.recipeservice.events;

public class BusinessException extends RuntimeException {
    public BusinessException(String exceptionOccured) {
        super(exceptionOccured);
    }
}
