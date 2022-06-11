package com.mt.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.mapper.FormMapper;
import com.mt.mapper.InverterMapper;
import com.mt.pojo.Form;
import com.mt.pojo.Inverter;
import com.mt.utils.CalculationUtils;
import com.mt.utils.Result;
import com.mt.vo.FormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/form")
@Api(tags = "表格信息")
public class FormController {

    @Autowired
    private FormMapper formMapper;

    @ApiOperation(value="根据id查询")
    @RequestMapping(value = "selectById", method = RequestMethod.GET)
    public Result<Object> selectById(
            @ApiParam(value="方案id") Integer programme_id) {
        try {
            Map<String,Object> map = new LinkedHashMap<>();

            Form Enable = formMapper.selectOne(new QueryWrapper<Form>()
                    .eq("programme_id",programme_id)
                    .eq("chose",true));
            map.put("Enable",Enable);
            Form Deactivate = formMapper.selectOne(new QueryWrapper<Form>()
                    .eq("programme_id",programme_id)
                    .ne("chose",true));
            if (Deactivate != null)
                map.put("Deactivate",Deactivate);
            return Result.success(map);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
}
