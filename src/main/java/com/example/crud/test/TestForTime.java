package com.example.crud.test;

import org.springframework.util.StopWatch;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/2/2 11:27
 * @version: V1.0
 */
public class TestForTime {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());//格式化输出
    }
}
