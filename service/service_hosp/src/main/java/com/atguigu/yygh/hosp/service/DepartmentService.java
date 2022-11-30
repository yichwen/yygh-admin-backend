package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    // 上传科室接口
    void save(Map<String, Object> paramMap);
    // 查询科室
    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);
    // 删除医院接口
    void remove(String hoscode, String depcode);
    //
    List<DepartmentVo> findDeptTree(String hoscode);
    //
    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
