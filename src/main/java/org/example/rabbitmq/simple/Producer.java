package org.example.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/*
所有中间件技术都是基于tcp/ip协议构建的新型协议，rabbitmq遵循的是amqp
ip port
1.创建链接工程
2.创建connection
3.通过链接获取通道 channel
4，通过创建交换机，声明队列，绑定关系，routing key，发送/接受消息
5.准备消息内容
6.发送消息队列
7.关闭链接
8.关闭通道

 */
public class Producer {
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
            connection = connectionFactory.newConnection("Producer");
            channel = connection.createChannel();
            /*
             * @param name:队列名称
             * @param 是否持久化（存盘）
             * @param 排他性，是否独占独立
             * @param 是否自动删除
             * @param map携带附属参数
             */
            channel.queueDeclare("queue1",false,false,false,null);
            String message = "hello rabbitmq";
            /*
             * @param 交换机名称，不存在没有交换机的队列，会有默认交换机
             * @param 队列名称
             * @param 消息状态控制，是否持久化
             * @param 消息内容
             */
            channel.basicPublish("","queue1",null,message.getBytes());
            System.out.println("Successfully sent message: " + message);
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
