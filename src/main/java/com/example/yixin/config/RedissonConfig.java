package com.example.yixin.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redissonClient 依赖低版本不匹配，注入不成功，手动注入
 * 自动注入支持 redisson 版本为  3.19.0  -  3.27.0
 * spring boot 2 不清楚为什么不支持自动注入，手动注入可以
 *
 * @author 亦-Nickname
 */
@Configuration
@Data
public class RedissonConfig {

    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String pwd;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
        //config.setCodec(new org.redisson.client.codec.StringCodec());
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://" + redisHost + ":" + redisPort)
                .setPassword(pwd)
                .setConnectionPoolSize(50)
                .setIdleConnectionTimeout(10000);

        return Redisson.create(config);
    }
}