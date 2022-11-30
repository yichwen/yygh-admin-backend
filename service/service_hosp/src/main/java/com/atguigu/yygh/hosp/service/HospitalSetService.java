package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    // get signing key by hospital code
    String getSignKey(String hoscode);

    SignInfoVo gtSignInfoVo(String hoscode);
}
