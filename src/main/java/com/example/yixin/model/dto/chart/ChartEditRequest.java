package com.example.yixin.model.dto.chart;


import lombok.Data;

import java.io.Serializable;


/**
 * 编辑请求
 *
 * @author 亦-Nickname
 */
@Data
public class ChartEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}