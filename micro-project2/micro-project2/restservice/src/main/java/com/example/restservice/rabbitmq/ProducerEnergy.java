package com.example.restservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ProducerEnergy {
    @Autowired
    private AmqpTemplate directExchangeTemplate;

    @Value("${spring.rabbitmq.direct-exchange}")
    private String directExchangeEnergy;

    @Value("${spring.rabbitmq.all-queue}")
    private String eQueue;

    @Value("${spring.rabbitmq.e-routing-key}")
    private String eRoutingKey;

    @Bean
    public Queue eQueue() {
        return new Queue(eQueue, false);
    }


    public void sendLog(String msg){
        directExchangeTemplate.convertAndSend(directExchangeEnergy, eRoutingKey, msg);
    }
    @Bean
    public DirectExchange directExchangeEnergy() {
        return new DirectExchange(directExchangeEnergy);
    }

    @Bean
    public Binding directBindingEnergy(Queue eQueue, DirectExchange directExchangeEnergy) {
        return BindingBuilder.bind(eQueue).to(directExchangeEnergy).with(eRoutingKey);
    }

}
