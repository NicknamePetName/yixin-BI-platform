package com.example.yixin.mq;

import com.example.yixin.api.AzureOpenAI;
import com.example.yixin.common.ErrorCode;
import com.example.yixin.constant.BiMqConstant;
import com.example.yixin.exception.BusinessException;
import com.example.yixin.manager.AiManager;
import com.example.yixin.model.entity.Chart;
import com.example.yixin.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BiMessageConsumer {

    @Autowired
    private ChartService chartService;

    @Autowired
    private AzureOpenAI azureOpenAI;

    @Autowired
    private AiManager aiManager;

    @Autowired
    private RedisTemplate redisTemplate;


    // 指定程序监听的消息队列和确认机制
    @SneakyThrows // 不想捕获异常( channel.basicAck(deliverTag, false) 的异常)
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) {
        log.info("receiveMessage message = {}",message);

        if (StringUtils.isBlank(message)) {
            // 消息拒绝，不放回队列
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        Long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图表为空");
        }

        // 先修改图标任务状态为“执行中”，等执行成功后，修改为“已完成”，保存执行结果，执行失败后，状态修改为“失败”，记录任务失败信息。

        Chart updateChar = new Chart();
        updateChar.setId(chart.getId());
        updateChar.setStatus("running");
        boolean isSuccessful = chartService.updateById(updateChar);
        if (!isSuccessful) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表为执行中状态失败");
            return;
        }
        

        // 知识星球 AI 模型
//        String result = aiManager.doChat(CommonConstant.BI_MODEL_ID, buildUserInput(chart));
        // 微软 azure openai 模型
        String result = azureOpenAI.AzureOpenAIChat(buildUserInput(chart),chart.getUserId());
        String[] splits = result.split("【【【【【");
        if (splits.length != 3) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "AI 生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        Chart chartResult = new Chart();
        chartResult.setId(chart.getId());
        chartResult.setGenChart(genChart);
        chartResult.setGenResult(genResult);
        // todo 状态可以定义为枚举值
        chartResult.setStatus("succeed");
        boolean isTrue = chartService.updateById(chartResult);
        if (!isTrue) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表为成功状态失败");
        }
        

        log.error("receiveMessage message = {}",message);
        channel.basicAck(deliverTag, false);
    }


    /**
     * 构建用户输入
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {

        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 用户输入
        StringBuilder userInput = new StringBuilder();

        userInput.append("分析需求:").append("\n");
        userInput.append(goal).append("\n");
        userInput.append("图表类型:").append("\n");
        userInput.append(chartType).append("\n");
        userInput.append("原始数据:").append("\n");
        userInput.append(csvData).append("\n");

        return userInput.toString();
    }


    private void handleChartUpdateError(long chartId,String execMessage) {
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setStatus("failed");
        updateChart.setExecMessage(execMessage);
        boolean isSuccessful = chartService.updateById(updateChart);
        if (!isSuccessful) {
            log.error("更新图表、状态失败" + chartId + "," + execMessage);
        }
        
    }

}
