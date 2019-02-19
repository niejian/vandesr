package cn.com.vandesr.backend.config.aop;

import cn.com.vandesr.backend.config.utils.CommonFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author: nj
 * @date: 2019/2/12:下午3:16
 */
@Slf4j
@Aspect
@Component
public class LogAspectImpl {

    /**
     * 切面注解，
     */
    @Pointcut("@annotation(cn.com.vandesr.backend.config.aop.LogAspect)")
    public void logAspect() {

    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before(value = "logAspect()")
    public void logBefore(JoinPoint joinPoint) {
        //获取请求链接，并记录之
        //获取当前的请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();

        //获取请求参数，并记录之
        Object[] args = joinPoint.getArgs();
        if (null != args && args.length > 0) {
            //请求参数都是json格式
            for (Object obj : args) {
                //只要参数不是request或者response类型。就输出到日志中
                CommonFunction.beforeProcess(log, uri, obj.toString());
//                if (!(obj instanceof HttpServletRequest)
//                        && !(obj instanceof HttpServletResponse)) {
//                    log.info("请求链接--->{}，参数：{}");
//                    CommonFunction.beforeProcess(log, uri, obj.toString());
//
//
//                }
            }
        }
    }

    /**
     * 后置处理
     */
    @After(value = "logAspect()")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        log.info("调用方法{}结束, 返回结果：{}", methodName, args.toString());

    }
}
