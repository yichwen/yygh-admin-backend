package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    // inject service
    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置信息")
    @GetMapping("findAll")
    public Result<List<HospitalSet>> findAllHospitalSet() {
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return Result.ok(hospitalSetList);
    }

    @ApiOperation(value = "逻辑删除医院设置信息")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean removed = hospitalSetService.removeById(id);
        return removed ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "条件查询")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable int current,
                                  @PathVariable int limit,
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // create Page
        Page<HospitalSet> page = new Page<>(current, limit);
        // create condition
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        if (hospitalSetQueryVo != null) {
            String hoscode = hospitalSetQueryVo.getHoscode();   // Hospital Code
            String hosname = hospitalSetQueryVo.getHosname();   // Hospital Name
            if (!StringUtils.isEmpty(hosname)) {
                wrapper.like("hosname", hospitalSetQueryVo.getHosname());
            }
            if (!StringUtils.isEmpty(hoscode)) {
                wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
            }
        }
        // invoke method to query
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }

    @ApiOperation(value = "添加医院设置信息")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 1-可以使用, 0-不能使用
        hospitalSet.setStatus(1);
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean saved = hospitalSetService.save(hospitalSet);
        return saved ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据ID获取设置信息")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    @ApiOperation(value = "修改设置信息")
    @PostMapping("updateHospitalSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet) {
        boolean updated = hospitalSetService.updateById(hospitalSet);
        return updated ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "批量删除设置信息")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> list) {
        hospitalSetService.removeByIds(list);
        return Result.ok();
    }

    @ApiOperation(value = "锁定与解锁医院设置")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id, @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    @ApiOperation(value = "发送签名密钥")
    @GetMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hostcode = hospitalSet.getHoscode();
        // TODO: send key via sms
        return Result.ok();
    }

}
