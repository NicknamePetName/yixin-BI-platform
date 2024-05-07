package com.example.yixin.manager;

import com.example.yixin.common.ErrorCode;
import com.example.yixin.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AiManager {
    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * AI 对话
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(Long modelId,String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
//        devChatRequest.setModelId(1785187475477090306L);  1659171950288818178L
        devChatRequest.setModelId(modelId);//1659171950288818178L
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
