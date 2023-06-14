package com.example.restservice.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class ProducerE {

    public void sendMsg(String msg) throws IOException, TimeoutException {
        //String queueName = "kolejka2";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("DIRECT", BuiltinExchangeType.DIRECT);

       // channel.queueDeclare(queueName, false, false, false, null);

        channel.basicPublish("DIRECT", "energy", null, msg.getBytes());
        connection.close();
    }
}
