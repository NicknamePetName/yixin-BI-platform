package com.example.yixin.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AzureOpenAITest {

    @Autowired
    private AzureOpenAI azureOpenAI;

    @Test
    void azureOpenAIChat() {
        System.out.println(azureOpenAI.AzureOpenAIChat("分析需求：\n" +
                "分析网站用户的增长情况\n" +
                " 原始数据：\n" +
                " 日期,用户数\n" +
                " 1号,10\n" +
                " 2号,20\n" +
                " 3号,30"));
    }
}