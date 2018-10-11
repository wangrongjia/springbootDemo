package com.codinger.manyToMany;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "codinger")
public class CodingerReceiver2 {

    @RabbitHandler
    public void process(String codinger) {
        System.out.println("Receiver 2: " + codinger);
    }

}
