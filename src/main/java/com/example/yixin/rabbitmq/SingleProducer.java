package com.example.yixin.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class SingleProducer {


    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 服务器需要改
        factory.setHost("localhost");
//        factory.setUsername("");
//        factory.setPassword("");
//        factory.setPort(0);
        // 建立连接、创建频道
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 创建消息队列
            // 消息队列重启后，消息队列名称; 消息是否持久化; 是否只允许当前这个创建消息队列的连接操作消息队列; 没有人使用消息队列，是否删除队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送消息
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}