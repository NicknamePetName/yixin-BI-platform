package com.example.yixin.api;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AzureOpenAI {
    @Value("${azure.openai.api.key}")
    private String OPENAI_API_KEY;
    @Value("${azure.openai.endpoint}")
    private String ENDPOINT;
    @Value("${azure.openai.model.id}")
    private String DEPLOYMENT_OR_MODEL_ID;
    private static final String CHAT_ROLE_SYSTEM = "(中文回答)你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
            "分析需求：\n" +
            "{数据分析的需求或者目标}\n" +
            "原始数据：\n" +
            "{csv格式的原始数据，用,作为分隔符}\n" +
            "图表类型：\n" +
            "{指定的图表类型(未指定，默认条形图)}\n" +
            "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
            "【【【【【\n" +
            "{Echarts V5 的option配置对象代码(并转换为json格式，以便于JSON.parse函数的解析,不要生成option=)，需要表名，合理地将数据进行可视化，不要生成任何多余的内容注释}\n" +
            "【【【【【\n" +
            "{明确的数据分析结论、越详细越好，不要生成多余的注释}";


    public String AzureOpenAIChat(String userMessage) {

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(ENDPOINT)
                .credential(new AzureKeyCredential(OPENAI_API_KEY))
                .buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, CHAT_ROLE_SYSTEM));
        chatMessages.add(new ChatMessage(ChatRole.USER, userMessage));

        ChatCompletions chatCompletions = client.getChatCompletions(DEPLOYMENT_OR_MODEL_ID, new ChatCompletionsOptions(chatMessages));

        String result = String.valueOf(chatCompletions.getChoices().stream()
                .map(ChatChoice::getMessage)
                .map(ChatMessage::getContent)
                .collect(Collectors.toList()));

        if (result.startsWith("[") && result.endsWith("]")) {
            result = result.substring(1, result.length() - 1);
        }

        System.out.println(result);

        return result;
    }
}
