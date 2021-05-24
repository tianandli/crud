package com.example.crud.config;

import com.example.crud.common.init_destroy.GetDestroyWord;
import com.example.crud.common.init_destroy.GetStartWord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述：
 * 当springboot的run启动的时候，会去调用GetStartWord类中的start方法。即初始化方法
 * destroyMethod对应的end方法没有打印出来，不知道是什么效果，不知道什么原因。
 *
 * @author: lijie
 * @date: 2021/3/12 15:17
 * @version: V1.0
 */
@Configuration
public class InitConfig {

    @Bean(initMethod = "start")
    public GetStartWord getStartWord(){
        return new GetStartWord();
    }

    @Bean(destroyMethod = "end")
    public GetDestroyWord getEndWord(){
        return new GetDestroyWord();
    }
}
