package com.example.crud.common.schedule;

import com.example.crud.model.entity.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 功能描述：
 *
 * @author: lijie
 * @date: 2021/1/20 13:32
 * @version: V1.0
 */
@Component
public class SchedulerTask {
    /**
     * 表示每隔6秒发送一次邮件
     */
    @Scheduled(cron = "0 0 * * * ?")
    private void proces(){
        System.out.println("定时任务在跑"+ new Date());
    }
}
