package com.mt.controller;

import com.mt.mapper.InverterMapper;
import com.mt.pojo.Inverter;
import com.mt.utils.CalculationUtils;
import com.mt.utils.Result;
import com.mt.vo.Battery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inverter")
@Api(tags = "逆变器信息")
public class InverterController {

    @Autowired
    private InverterMapper inverterMapper;

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

    //查看所有逆变器
    @ApiOperation(value="根据逆变器以及发电量获得预期")
    @RequestMapping(value = "getCapacity", method = RequestMethod.GET)
    public Result<Map<String,Object>> getCapacity(
            @ApiParam(value="逆变器id") Integer inverter_id,
            @ApiParam(value="需求发电量") String capacity,
            @ApiParam(value="逆变器数量") Integer inverter_num) {
        try{
            Inverter inverter = inverterMapper.selectById(inverter_id);
            return Result.success(CalculationUtils.getGeneratingCapacity(inverter,capacity,inverter_num));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }



}
