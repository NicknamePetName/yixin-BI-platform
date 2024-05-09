package com.example.yixin.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class MultiConsumer {

  private static final String TASK_QUEUE_NAME = "multi_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    final Connection connection = factory.newConnection();

    // 测试多个消费者，快速验证队列模式工作机制：这里加 for 循环
    final Channel channel = connection.createChannel();
    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    // 控制单个消费者的处理任务挤压数(每个消费者最多同时处理 N 个任务)
    channel.basicQos(1);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");

        try {
            System.out.println(" [x] Received '" + message + "'");
            Thread.sleep(20000);
            // 指定确认某条消息, 参数二 multiple 批量确认：是否要一次性确认所有的历史消息直到当前着条
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 指定拒绝某条消息, 参数二 multiple 批量确认：是否要一次性拒绝所有的历史消息直到当前着条
            // 参数三 requeue 表示是否重新入队：可用于重试
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        } finally {
            System.out.println(" [x] Done");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    };
    // 开启消费监听 autoAck 建议为 false, 手动去确认某条消息
    channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> { });
  }

}