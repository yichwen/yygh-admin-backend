package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    // 上传医院接口
    void save(Map<String, Object> paramMap);
    // 用编号获取医院信息
    Hospital getByHoscode(String hoscode);
    // 条件查询带分页
    Page<Hospital> selectHospPage(int page, int limit, HospitalQueryVo hospitalQueryVo);
    // 更新医院状态
    void updateStatus(String id, Integer status);
    // 根据id获取医院详情
    Map<String, Object> getHospitalById(String id);
    // 根据编号得到医院名称
    String getHospName(String hoscode);

    // 根据医院名称查询
    List<Hospital> findByHosname(String hosname);
    // 根据医院编号获取详情
    Map<String, Object> item(String hoscode);
}
