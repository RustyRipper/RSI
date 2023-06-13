package org.example;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class ConsumerI {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel4 = connection.createChannel();
        channel4.queueDeclare("kolejkaReq", false, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel4) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println(msg);
                Integer integer = Integer.parseInt(msg);
                System.out.println(envelope.getRoutingKey());
                byte[] response = getImageFromSyste(integer);

                channel4.basicPublish("", properties.getReplyTo(), properties, response);
//                channel4.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel4.basicConsume("kolejkaReq", true, consumer);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        connection.close();
    }

    private static byte[] getImageFromSyste(Integer integer) throws IOException {
        String imagePath = "C:\\git_projects\\RSI\\micro-project2\\micro-project2\\ConsumerI\\src\\main\\java\\org" +
                "\\example\\img\\" + integer.toString()+".jpg";
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }
}