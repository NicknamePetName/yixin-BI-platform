package com.example.yixin.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis(序列化) java_object ——> redis_json
 *
 * @author 亦-Nickname
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * 法一 启用循环依赖 添加
     *
     * spring.main.allow-circular-references=true
     */

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        redisTemplateInit();
    }

    @Bean
    public RedisTemplate redisTemplateInit() {
        //设置序列化Key的工具
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置序列化Value的工具
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 设置 hash 的 key
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        // 设置 hash 的 value
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    /**
     * 法 二
     */
//    private final RedisConnectionFactory redisConnectionFactory;
//
//    @Autowired
//    public RedisTemplateConfig(RedisConnectionFactory redisConnectionFactory) {
//        this.redisConnectionFactory = redisConnectionFactory;
//    }
//
//    @Bean
//    public RedisTemplate redisTemplateInit() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        //设置序列化Key的工具
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        //设置序列化Value的工具
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        // 设置 hash 的 key
//        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
//        // 设置 hash 的 value
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

}
