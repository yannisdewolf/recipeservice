package be.dewolf.recipes.recipeservice.controller.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RecipeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex,
                new CustomErrorMessage("no entity found", "ENTITY_NOT_FOUND"),
                HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request);
    }

    record CustomErrorMessage(String message, String code){};

}
