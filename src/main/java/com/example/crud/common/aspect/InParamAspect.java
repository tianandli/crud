package com.example.crud.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 功能描述：接口调用入参的切面
 *
 * @author: lijie
 * @date: 2021/1/25 14:24
 * @version: V1.0
 */
@Component
@Slf4j
@Aspect
public class InParamAspect {

    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.example.crud.controller.*Controller.*(..))")
    public void dmsLog(){

    }


    /**
     * 描述：接口调用前进来
     * @param joinPoint joinPoint
     * @return void
     * @author lijie
     * @date 2021/1/25 15:12
     */
    @Before(value = "dmsLog()")
    public void doBefore(JoinPoint joinPoint) throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] args = joinPoint.getArgs();
        for(Object object : args){
            log.info("参数是："+object);
        }
        ResponseDataBody responseDataBody = new ResponseDataBody();
        responseDataBody.setSysDate(String.valueOf(new Date()));
        AppContextHolder.set(responseDataBody);
        log.info(responseDataBody.toString()+"11111");
    }

    /**
     * 描述：接口调用成功进来
     * @param joinPoint, joinPoint
     * @param ret, ret
     * @return void
     * @author lijie
     * @date 2021/1/25 15:13
     */
    @AfterReturning(returning = "ret", pointcut = "dmsLog()")
    public void doAfter(JoinPoint joinPoint, Object ret){
        ResponseDataBody responseDataBody = AppContextHolder.getCurrentContext();
        if(responseDataBody != null){
            log.info(responseDataBody.getSysDate()+"22222");
        }
    }
}
