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
public class ProducerA {

    public void sendMsg(String msg) throws IOException, TimeoutException {
        String queueNameA = "kolejka";
        ConnectionFactory factory2 = new ConnectionFactory();
        factory2.setHost("localhost");
        Connection connection2 = factory2.newConnection();
        Channel channel2 = connection2.createChannel();
        channel2.exchangeDeclare("DIRECT", BuiltinExchangeType.DIRECT);
        channel2.queueDeclare(queueNameA, false, false, false, null);

        channel2.basicPublish("DIRECT", "all", null, msg.getBytes());
        connection2.close();
    }
}
