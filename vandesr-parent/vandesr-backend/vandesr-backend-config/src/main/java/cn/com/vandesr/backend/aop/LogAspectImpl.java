package cn.com.vandesr.backend.aop;

import cn.com.vandesr.backend.config.utils.CommonFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: nj
 * @date: 2019-07-26:15:38
 */
@Slf4j
@Aspect
@Component
public class LogAspectImpl {

    @Pointcut("@annotation(cn.com.vandesr.backend.aop.LogAspect)")
    public void logAspect() {

    }

    /**
     * 日志前置通知
     * @param joinPoint
     */
    @Before(value = "logAspect()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("log before");
        //获取请求链接，并记录之
        //获取当前的请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();

        //获取请求参数，并记录之
        Object[] args = joinPoint.getArgs();
        if (null != args && args.length > 0) {
            CommonFunction.beforeProcess(log, args);

            //请求参数都是json格式
            for (Object obj : args) {
                //只要参数不是request或者response类型。就输出到日志中
                if (!(obj instanceof HttpServletRequest)
                        && !(obj instanceof HttpServletResponse)) {
                    //log.info("请求链接--->{}，参数：{}", uri, obj.toString());
//                    if (obj instanceof Integer) {
//                        log.info("请求链接--->{}，参数：{}", uri, obj);
//                    } else if (obj instanceof JSONObject) {
//                        log.info("请求链接--->{}，参数：{}", uri, );
//                    }

                }
            }
        }
    }

    @AfterThrowing(value = "logAspect()", throwing = "ex")
    public void throwLog(Throwable ex) {
        log.info("log error");
        CommonFunction.afterProcess(log, ex);

    }

}
