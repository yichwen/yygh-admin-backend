package com.atguigu.yygh.task.schedule;

import com.atguigu.common.rabbit.constant.MqConst;
import com.atguigu.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private RabbitService rabbitService;

    // cron表达式，设置执行间隔
    // 0 0 8 * * ?  // 每天8点执行
    @Scheduled(cron = "0/30 * * * * * ?")  // 每30秒执行 测试
    public void taskPatient() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }

}
