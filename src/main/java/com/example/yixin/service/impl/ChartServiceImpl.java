package com.example.yixin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yixin.model.entity.Chart;
import com.example.yixin.service.ChartService;
import com.example.yixin.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author 亦-Nickname
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-04-27 13:06:43
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




