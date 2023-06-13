package com.example.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = "${spring.rabbitmq.direct-queue}")
    public void processAllMessage(String message) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("Received all message: " + message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.fanout-queue}")
    public void processEnergyMessage(String message) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("Received energy message: " + message);
    }
}
