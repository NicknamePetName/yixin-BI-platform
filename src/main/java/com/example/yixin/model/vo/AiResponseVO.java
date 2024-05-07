package com.example.yixin.model.vo;

import lombok.Data;

/**
 * Ai 的返回结果
 */
@Data
public class AiResponseVO {
    private String genChart;
    private String genResult;
    private Long chartId;
}
