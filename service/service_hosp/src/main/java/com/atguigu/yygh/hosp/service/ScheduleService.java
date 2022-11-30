package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleOrderVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);
    void remove(String hoscode, String hosScheduleId);
    Map<String, Object> getScheduleRule(long page, long limit, String hoscode, String depcode);
    //查询排班详细信息
    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);
    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
    Schedule getScheduleById(String scheduleId);
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    void update(Schedule schedule);
}
