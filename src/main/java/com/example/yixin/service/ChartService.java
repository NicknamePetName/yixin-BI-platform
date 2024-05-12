package com.example.yixin.service;

import com.example.yixin.model.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 亦-Nickname
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-04-27 13:06:43
*/
public interface ChartService extends IService<Chart> {
    String KEY_CHART = "chartList";
}
