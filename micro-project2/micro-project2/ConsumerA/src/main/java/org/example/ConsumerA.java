package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumerA {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("DIRECT", BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        String[] routingKeys = new String[]{"non-energy", "energy"};
        for(String key: routingKeys){
            channel.queueBind(queueName, "DIRECT", key);
        }

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println(msg);
                System.out.println(envelope.getRoutingKey());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        connection.close();
    }
}