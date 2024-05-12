package com.example.yixin.mq;

import com.example.yixin.constant.BiMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class BiMessageProducer {

    @Value("${spring.rabbitmq.host}")
    private String localhost;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    private void init() {
        Object isInit = redisTemplate.opsForValue().get("initRabbitMq");
        if (isInit == null) {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(BiMqConstant.BI_EXCHANGE_NAME, "direct");

                // 创建队列，随机分配一个队列名称
                String queueName = BiMqConstant.BI_QUEUE_NAME;
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, BiMqConstant.BI_EXCHANGE_NAME,  BiMqConstant.BI_ROUTING_KEY);
                redisTemplate.opsForValue().set("initRabbitMq",true);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("RabbitMq Init Error !");
            }
        }
    }

    /**
     * 发送消息
     * @param message
     */

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY, message);
    }

}
