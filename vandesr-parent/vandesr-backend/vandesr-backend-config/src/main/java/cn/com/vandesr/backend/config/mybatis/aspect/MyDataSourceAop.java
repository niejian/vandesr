package cn.com.vandesr.backend.config.mybatis.aspect;

import cn.com.vandesr.backend.config.mybatis.datasource.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: nj
 * @date: 2019/1/21:下午1:41
 */
@Aspect
@Component
public class MyDataSourceAop {
    @Pointcut("!@annotation(cn.com.vandesr.backend.config.mybatis.annotation.Master) " +
            "&& (execution(* cn.com.vandesr.*.service..*.select*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.get*(..)))" +
            "|| execution(* cn.com.vandesr.*.service..*.query*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(cn.com.vandesr.backend.config.mybatis.annotation.Master) " +
            "|| execution(* cn.com.vandesr.*.service..*.insert*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.add*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.update*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.edit*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.delete*(..)) " +
            "|| execution(* cn.com.vandesr.*.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }
}
