package com.example.yixin.mapper;

import com.example.yixin.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
* @author 亦-Nickname
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-04-27 13:06:43
* @Entity generator.domain.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {
    /**
     * @param chartId
     * @return
     */
    List<Map<String, Object>> queryChartData(String chartId);


}




