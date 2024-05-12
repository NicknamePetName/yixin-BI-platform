package com.example.yixin.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 用于创建测试程序用到的交换机和队列(只用执行一次)
 */
public class InitMq {

    private static final String EXCHANGE_NAME = "code_exchange";

    public static void main(String[] args) {

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // 创建队列
            // String queueName = channel.queueDeclare().getQueue();
            String queueName = "code_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");  // 创建队列
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
