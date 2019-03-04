package com.dx.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;

import java.sql.SQLException;

/**
 * 和事务管理相关的工具类，它包含了，开启事务、提交事务、回滚事务和释放事务
 * 线程用完之后要与连接进行解绑操作，
 * 在事务管理类中注入连接，方便对连接的管理操作
 */
@Component("manager")
@Aspect
public class TransactionManagerUtils {

    @Autowired
    private ConnectionUtils connectionUtils;

    /**
     * 切入点表达式
     */
    @Pointcut("execution(* com.dx.service.impl.*.*(..))")
    private void pt1(){}

    public void begin(){
        try {
            connectionUtils.getThreadConnection().setAutoCommit(false);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 提交事务
     */
    public void commit(){
        try {
            connectionUtils.getThreadConnection().commit();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 回滚事务
     */
    public void rollback(){
        try {
            connectionUtils.getThreadConnection().rollback();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void release(){
        try {
            connectionUtils.getThreadConnection().close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 环绕通知
     * @param pjp
     * @return
     */
    @Around("pt1()")
    public Object aroundAdvice(ProceedingJoinPoint pjp){
        Object rtValue = null;
        try{
            //获取参数
            Object[] args = pjp.getArgs();
            //开启注解
            this.begin();
            //执行方法
            rtValue = pjp.proceed();
            //提交
            this.commit();
        }catch (Throwable t){
            this.rollback();
            throw new RuntimeException(t);
        }finally {
            this.release();
        }
        return rtValue;
    }
}
