package com.mt.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.mapper.FormMapper;
import com.mt.mapper.InverterMapper;
import com.mt.mapper.ProgrammeMapper;
import com.mt.pojo.Form;
import com.mt.pojo.Inverter;
import com.mt.pojo.Programme;
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
    @Autowired
    private ProgrammeMapper programmeMapper;

    @ApiOperation(value="根据id查询  如果方案下有两个方案两个都返回")
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

    @ApiOperation(value="选择方案")
    @RequestMapping(value = "choseForm", method = RequestMethod.POST)
    public Result<Object> choseForm(
            @ApiParam(value="方案id") Integer form_id) {
        try {
            Form form = formMapper.selectById(form_id);
            if (form!= null){
                form.setChose(true);
                formMapper.updateById(form);
                Programme programme = programmeMapper.selectById(form.getProgramme_id());
                programme.setDemand_capacity(form.getDemand_capacity());
                programme.setActual_capacity(form.getActual_capacity());
                programmeMapper.updateById(programme);
                return Result.success();
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }


}
