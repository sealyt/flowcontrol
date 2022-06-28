package org.example.flowcontrol.core;

import lombok.extern.slf4j.Slf4j;
import org.example.flowcontrol.util.RedisUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 限流拦截器
 */
@Slf4j
public class FlowControlInterceptor implements HandlerInterceptor {

    /** 限流redis key **/
    private final String FLOW_CONTROL_KEY = "flowControlKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 项目中这三个参数可以配置到数据库
        // 限流时长
        int duration = 1;
        // 最大访问次数
        int count = 1000;
        // 限流时长单位
        TimeUnit timeUnit = TimeUnit.SECONDS;
        Boolean aBoolean = RedisUtils.rateLimiter(FLOW_CONTROL_KEY, count, duration, timeUnit);
        if (!aBoolean){
            log.info("服务繁忙请稍后重试~~");
            throw new RuntimeException("服务繁忙请稍后重试~~");
        }
        return true;
    }
}
