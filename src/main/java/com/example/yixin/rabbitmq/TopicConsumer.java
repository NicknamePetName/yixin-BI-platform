package com.example.yixin.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TopicConsumer {

  private static final String EXCHANGE_NAME = "topic-exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "topic");

      // 创建队列
      // String queueName = channel.queueDeclare().getQueue();
      String queueName = "topic_queue";
      channel.queueDeclare(queueName, true, false, false, null);
      channel.queueBind(queueName, EXCHANGE_NAME, "#.前端.#");  // 创建队列

      String queueName2 = "topic2_queue";
      channel.queueDeclare(queueName2, true, false, false, null);
      channel.queueBind(queueName2, EXCHANGE_NAME, "#.后端.#");  // 创建队列

      String queueName3 = "topic3_queue";
      channel.queueDeclare(queueName3, true, false, false, null);
      channel.queueBind(queueName3, EXCHANGE_NAME, "#.产品.#");  // 创建队列

      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [小a] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };


      DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [小b] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };

      DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [小c] Received '" +
                  delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
      };
      channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
      });
      channel.basicConsume(queueName2, true, deliverCallback2, consumerTag -> {
      });
      channel.basicConsume(queueName3, true, deliverCallback3, consumerTag -> {
      });

  }
}