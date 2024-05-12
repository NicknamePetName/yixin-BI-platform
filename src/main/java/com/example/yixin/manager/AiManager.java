package com.example.yixin.manager;

import com.example.yixin.common.ErrorCode;
import com.example.yixin.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AiManager {

    @Autowired
    private RedisLimiterManager redisLimiterManager;
    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * AI 对话
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(Long modelId,String message) {
        // 限流，每天三十次 redissonClient.getAtomicLong(now)
        redisLimiterManager.getAutoIncrId();

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
