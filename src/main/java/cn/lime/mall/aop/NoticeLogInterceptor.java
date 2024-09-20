package cn.lime.mall.aop;


import cn.lime.core.threadlocal.ReqInfo;
import cn.lime.core.threadlocal.ReqThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: LogInterceptor
 * @Description: 日志
 * @Author: Lime
 * @Date: 2023/11/8 15:41
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class NoticeLogInterceptor {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 切点
     */
    @Pointcut("within(@cn.lime.mall.annotation.NoticeLog *)")
    public void webLog() {
    }

    /**
     * Log打印
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //打印日志
        log.info("Get wx Notice {}",sdf.format( new Date()));


    }

    /**
     * Log打印
     */
    @After("webLog()")
    public void afterTime() {
        log.info("Finish wx Notice {}",sdf.format( new Date()));
    }

}
