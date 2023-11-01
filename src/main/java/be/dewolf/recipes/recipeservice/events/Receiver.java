package be.dewolf.recipes.recipeservice.events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  public void receiveMessage(String message) {

    System.out.println("Received <" + message + ">");
    throw new BusinessException("exception occured");
  }

  @RabbitListener(queues = "${app.rabbit.recipeCreatedListener.queueName}",
    containerFactory = "")
  public void receiveMessage2() throws Exception {

  }


}