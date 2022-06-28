package org.example.flowcontrol.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Slf4j
@Component
public class RedisUtils {

    private static StringRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate){
        RedisUtils.redisTemplate = redisTemplate;
    }

    /**
     * 限流器
     * 判断同一个key在规定时间内访问次数是否到达了最高值
     * @param key   键
     * @param duration  时长
     * @param count 一定时间内的访问次数
     * @param timeUnit 时间类型
     * @return boolean
     */
    public static Boolean rateLimiter(String key, int count, int duration, TimeUnit timeUnit) {
        // 限流初始值
        String initRequestCount = "1";

        // 判断在redis中是否有key值
        Boolean redisKey = redisTemplate.hasKey(key);
        if (Objects.nonNull(redisKey) && redisKey) {
            // 获取key所对应的value
            String value = redisTemplate.opsForValue().get(key);
            if (Objects.isNull(value)){
                redisTemplate.opsForValue().set(key, initRequestCount, duration, timeUnit);
                return true;
            }

            int requestCount =Integer.parseInt(value);
            log.info("请求次数：" + requestCount);
            if (requestCount >= count) {
                return false;
            }
            // 对value进行加1操作
            redisTemplate.opsForValue().increment(key,1);
            return true;
        }else {
            // 如果没有key值，对他进行添加到redis中
            redisTemplate.opsForValue().set(key, initRequestCount, duration, timeUnit);
        }
        return true;
    }
}
