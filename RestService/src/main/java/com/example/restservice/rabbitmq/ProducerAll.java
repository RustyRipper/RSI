package com.example.restservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class ProducerAll {
    private final AmqpTemplate directExchangeTemplate;

    @Value("${spring.rabbitmq.direct-exchange}")
    private String directExchange;

    @Value("${spring.rabbitmq.all-queue}")
    private String allQueue;

    @Value("${spring.rabbitmq.all-routing-key}")
    private String allRoutingKey;

    public ProducerAll(AmqpTemplate directExchangeTemplate) {
        this.directExchangeTemplate = directExchangeTemplate;
    }


    public void sendLog(String msg){
        directExchangeTemplate.convertAndSend(directExchange, allRoutingKey, msg);
    }

    @Bean
    public Queue allQueue() {
        return new Queue(allQueue, false);
    }
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange);
    }

    @Bean
    public Binding directBinding(Queue allQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(allQueue).to(directExchange).with(allRoutingKey);
    }

}
