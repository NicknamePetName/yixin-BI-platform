package com.example.yixin.manager;

import com.example.yixin.common.ErrorCode;
import com.example.yixin.exception.BusinessException;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 专门提供 RedisLimiter 限流基础服务的（提供了通用的能力）
 *
 * 用户限流 + 每天限流
 *
 */
@Service
public class RedisLimiterManager {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 限流操作 2/s
     *
     * @param key 区分不同的限流器，比如不同的用户 id 应该分别统计
     */
    public void userCurrentLimiting(String key) {
        // 创建一个名称为user_limiter的限流器，每秒最多访问 2 次
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        // 每当一个操作来了后，请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }

    /**
     * 限流 30/day
     * redissonClient.getAtomicLong(now)
     */

    public void getAutoIncrId() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = LocalDate.now().format(dateTimeFormatter);
        RAtomicLong atomicLong = redissonClient.getAtomicLong(now);
        atomicLong.expire(1, TimeUnit.DAYS);
//        long number = atomicLong.incrementAndGet();
//        String orderId = now + String.format("%08d", number);
//        return orderId;
         if (atomicLong.incrementAndGet() > 100) {
             throw new BusinessException(ErrorCode.TOO_END_REQUEST);
         }
    }


}