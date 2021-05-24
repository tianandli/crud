package com.example.crud.common.interceptor;

import com.example.crud.common.response.BaseResponse;
import com.example.crud.model.vo.UserContext;
import com.example.crud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 功能描述：拦截器，每个接口调用之前都会先走这个拦截器，用它来统一拦截判断是否已登录
 *
 * @author: lijie
 * @date: 2021/1/26 10:22
 * @version: V1.0
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 简单的白名单，登录这个接口直接放行
        if ("/login".equals(request.getRequestURI())) {
            return true;
        }

        // 从请求头中获取token字符串并解析
        Claims claims = JwtUtil.parse(request.getHeader("Authorization"));
        // 已登录就直接放行
        if (claims != null) {
            // 将我们之前放到token中的userName给存到上下文对象中
            UserContext.add(claims.getSubject());
            log.info("claims.getSubject()是：{}", claims.getSubject());
            return true;
        }

        // 走到这里就代表是其他接口，且没有登录
        // 设置响应数据类型为json（前后端分离）
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        BaseResponse baseResponse = BaseResponse.ErrorInstance(400, "请先登录");

        // 设置响应内容，结束请求
        out.write(baseResponse.toString());
        out.flush();
        out.close();
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后要从上下文对象删除数据，如果不删除则可能会导致内存泄露
        UserContext.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
