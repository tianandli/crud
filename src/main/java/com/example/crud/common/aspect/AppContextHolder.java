package com.example.crud.common.aspect;


import org.springframework.context.ApplicationContext;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/1/25 14:42
 * @version: V1.0
 */
public class AppContextHolder {
    private static final ThreadLocal<ResponseDataBody> THREAD_LOCAL = new ThreadLocal<ResponseDataBody>(){
        protected ResponseDataBody init(){
            return new ResponseDataBody();
        }
    };
    public static ResponseDataBody getCurrentContext(){
        return THREAD_LOCAL.get();
    }
    public static ThreadLocal<ResponseDataBody> getThreadLocal() {
        return THREAD_LOCAL;
    }
    public static void remove(){
        THREAD_LOCAL.remove();
    }
    public static void set(ResponseDataBody responseDataBody){
        THREAD_LOCAL.set(responseDataBody);
    }


    private static ApplicationContext applicationContext;
    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }
    public static Object getBean(String beanId){
        return applicationContext.getBean(beanId);
    }
}
