package be.dewolf.recipes.recipeservice.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
public class Receiver {

    public String receiveMessage(String message) {
        log.info("Received <" + message + ">");
        return message;
    }

//    @RabbitListener(queues = "${app.rabbit.recipeCreatedListener.queueName}",
//            containerFactory = "")
//    public void receiveMessage2(String message) throws Exception {
//        log.info("received {}", message);
//    }


}