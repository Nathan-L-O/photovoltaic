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

import java.util.*;

@RestController
@RequestMapping("/inverter")
@Api(tags = "逆变器信息")
public class InverterController {

    @Autowired
    private InverterMapper inverterMapper;
    @Autowired
    private FormMapper formMapper;

    //添加逆变器
    @ApiOperation(value="添加逆变器")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(
            @RequestBody Inverter inverter) {
        int flag = inverterMapper.insert(inverter);
        if (1 == flag){
            return Result.success("添加成功");
        }else {
            return Result.success();
        }
    }

    //查看所有逆变器
    @ApiOperation(value="查看所有逆变器")
    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    public Result getAll() {
        try{
            List<Inverter> list = inverterMapper.selectList(null);
            return Result.success(list);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

}
