package com.example.yixin.controller;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author 亦-Nickname
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 图标名称
     */
    private String name;

    /**
     * 图标目标
     */
    private String goal;

    /**
     * 图标类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}