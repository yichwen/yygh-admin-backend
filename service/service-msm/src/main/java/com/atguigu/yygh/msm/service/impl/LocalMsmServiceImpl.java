package com.atguigu.yygh.msm.service.impl;

import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.vo.msm.MsmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
@Slf4j
public class LocalMsmServiceImpl implements MsmService {

    @Override
    public boolean send(String phone, String code) {
        log.info("code [" + code + "] is sent to phone [" + phone + "]");
        return true;
    }

    @Override
    public boolean send(MsmVo msmVo) {
        if (!StringUtils.isEmpty(msmVo.getPhone())) {
            return send(msmVo.getPhone(), msmVo.getParam());
        }
        return false;
    }

    private boolean send(String phone, Map<String, Object> param) {
        log.info("param " + param + " is sent to phone [" + phone + "]");
        return true;
    }

}
