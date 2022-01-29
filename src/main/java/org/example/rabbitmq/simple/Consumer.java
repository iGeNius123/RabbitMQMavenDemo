package org.example.rabbitmq.simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.136.129");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;

        try{
            connection = connectionFactory.newConnection("Consumer");
            channel = connection.createChannel();
            /*
             * @param name:队列名称
             * @param 是否持久化（存盘）
             * @param 排他性，是否独占独立
             * @param 是否自动删除
             * @param map携带附属参数
             */
            channel.basicConsume("queue1", true, new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    try {
                        System.out.println("Received message: " + new String(delivery.getBody(), "UTF8"));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println("Failed to receive message");
                }
            });
            System.out.println("Successfully receive message!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(channel != null && channel.isOpen()){
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(connection != null && connection.isOpen()){
                try{
                    connection.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
