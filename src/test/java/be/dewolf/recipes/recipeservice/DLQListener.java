package be.dewolf.recipes.recipeservice;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class DLQListener {

    @RabbitListener(queues = "recipeservice.recipe-created.dlq")
    public String receiveDLQMessage(String message) {
        System.out.println(message);
        return message;
    }

}
