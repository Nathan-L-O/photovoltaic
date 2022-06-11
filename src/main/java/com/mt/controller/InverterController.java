package com.mt.controller;

import com.alibaba.fastjson.JSON;
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

    //查看所有逆变器
    @ApiOperation(value="根据逆变器以及发电量获得预期")
    @RequestMapping(value = "getCapacity", method = RequestMethod.GET)
    public Result<Object> getCapacity(
            @ApiParam(value="逆变器id") Integer inverter_id,
            @ApiParam(value="需求发电量") String capacity,
            @ApiParam(value="逆变器数量") Integer inverter_num) {
        try{
            Inverter inverter = inverterMapper.selectById(inverter_id);
            Map<String,Form> map = CalculationUtils.getGeneratingCapacity(inverter,capacity,inverter_num);
            List<Form> list = new ArrayList<>();
            if (map.get("practical")!=null){
                Form form = map.get("practical");
                formMapper.insert(form);
                list.add(form);
            }
            if (map.get("economic")!=null){
                Form form = map.get("economic");
                formMapper.insert(form);
                list.add(form);
            }
            if (list.size()==2){
                //输出的第一个为实际发电量-需求发电量最小的方案
                Collections.sort(list, Comparator.comparingInt(o -> (int) (Double.parseDouble(o.getDemand_capacity()) - Double.parseDouble(o.getActual_capacity()))));
                //如果第一个方案超出逆变器电压范围则将第二个方案设置为最优
                if (list.get(0).getErrmsg() != null && list.get(1).getErrmsg() == null){
                    Form form = list.get(1);
                    list.set(1,list.get(0));
                    list.set(0,form);
                }
            }
                return Result.success(list);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }



}
