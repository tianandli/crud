package com.example.crud.config;

import com.example.crud.common.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 功能描述：使LoginInterceptor拦截器生效的配置
 *
 * @author: lijie
 * @date: 2021/1/26 10:39
 * @version: V1.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //使登录的拦截器生效
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/loginError");
        super.addInterceptors(registry);
    }
}
