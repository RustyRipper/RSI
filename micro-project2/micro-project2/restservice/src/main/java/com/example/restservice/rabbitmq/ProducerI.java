package com.example.restservice.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
public class ProducerI implements AutoCloseable {
    String queueNameI = "kolejkaReq";
    ConnectionFactory factory3 = new ConnectionFactory();
    Connection connection3 = factory3.newConnection();
    Channel channel3 = connection3.createChannel();
    private CompletableFuture<byte[]> response = new CompletableFuture<>();

    public ProducerI() throws IOException, TimeoutException {
    }

    public byte[] getImg(String img) throws IOException, TimeoutException, ExecutionException, InterruptedException {

        factory3.setHost("localhost");


        System.out.println("getImg");
        String correlationID = UUID.randomUUID().toString();
        String replyQName = channel3.queueDeclare().getQueue();
        AMQP.BasicProperties props =
                new AMQP.BasicProperties.Builder().correlationId(correlationID).replyTo(replyQName).build();
        channel3.queueDeclare(queueNameI, false, false, false, null);

        channel3.basicPublish("", queueNameI, props, img.getBytes("UTF-8"));



        String tag = channel3.basicConsume(replyQName, true, (consumerTag, returnMsg) -> {
            if (returnMsg.getProperties().getCorrelationId().equals(correlationID))
                response.complete(returnMsg.getBody());
        }, consumerTag -> {
        });

        byte[] result = response.get();
        System.out.println(Arrays.toString(result));
        channel3.basicCancel(tag);
        return result;
    }

    @Override
    public void close() throws Exception {
        channel3.close();
        connection3.close();
    }
}
