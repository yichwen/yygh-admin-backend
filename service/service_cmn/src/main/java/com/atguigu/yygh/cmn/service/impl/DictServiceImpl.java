package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    // 存储进入缓存
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        // 根据parent_id获取子节点
        List<Dict> dictList = baseMapper.selectList(wrapper);
        // 判断子节点下面是否还有子节点
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean hasChildren = hasChildren(dictId);
            dict.setHasChildren(hasChildren);
        }
        return dictList;
    }

    // 判断id下面是否有子节点
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        int count = baseMapper.selectCount(wrapper);
        return  count > 0;
    }

    @Override
    public void exportDictData(HttpServletResponse response) {
        // configure download information
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        // query database
        List<Dict> dictList = baseMapper.selectList(null);
        // convert Dict to DictEeVo
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        for (Dict dict: dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    // 清空缓存
    @CacheEvict(value = "dict", allEntries = true)
    // 导入数据字典 将会一行一行读取 DictListener将执行处理数据
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            // 直接用value查询 如果没有提供dict_code
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            // 根据dict_code获取id
            Dict dict = getDictByDictCode(dictCode);
            Long parentId = dict.getId();
            // 用上级id和value进行查询
            Dict findDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return findDict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        // 例如: 用root查询可返回 > 省,医院等级,民族...
        Dict dict = this.getDictByDictCode(dictCode);
        return this.findChildData(dict.getId());
    }

    // 根据dict_code找出相应的数据字典
    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }

}
