package com.example.yixin.mq;

import com.example.yixin.constant.BiMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;
    @PostConstruct
    private void init() {
        Object isInit = redisTemplate.opsForValue().get("initRabbitMq_Test");
        if (isInit == null) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare("code_exchange", "direct");

                // 创建队列，随机分配一个队列名称
                String queueName = "code_queue";
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, "code_exchange",  "my_routingKey");
                redisTemplate.opsForValue().set("initRabbitMq_Test",true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("RabbitMq Init Error !");
            }
        }
    }

    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
