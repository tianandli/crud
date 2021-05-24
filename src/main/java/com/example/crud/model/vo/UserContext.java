package com.example.crud.model.vo;

/**
 * 功能描述：统一拦截做好之后接下来就是我们的上下文对象，JWT不像Session把用户信息直接存储起来，所以JWT的上下文对象要靠我们自己来实现。
 * 首先我们定义一个上下文类，这个类专门存储JWT解析出来的用户信息。我们要用到ThreadLocal，以防止线程冲突。这个类是final的
 *
 * @author: lijie
 * @date: 2021/1/26 10:47
 * @version: V1.0
 */
public final class UserContext {
    private static final ThreadLocal<String> user = new ThreadLocal<String>();

    public static void add(String userName) {
        user.set(userName);
    }

    public static void remove() {
        user.remove();
    }

    /**
     * @return 当前登录用户的用户名
     */
    public static String getCurrentUserName() {
        return user.get();
    }
}
