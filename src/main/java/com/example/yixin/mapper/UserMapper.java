package com.example.yixin.mapper;

import com.example.yixin.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 亦-Nickname
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-04-27 13:06:43
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    User findByIdUser(@Param("userAccount") String userAccount );
}




