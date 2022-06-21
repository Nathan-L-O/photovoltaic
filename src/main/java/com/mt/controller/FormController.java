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
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value="编辑表单")
    @RequestMapping(value = "updateForm", method = RequestMethod.POST)
    public Result<Object> updateForm(
            @RequestBody Form form) {
        try {
            int i = formMapper.updateById(form);
            Form otherForm = formMapper.selectOne(new QueryWrapper<Form>()
                    .eq("programme_id",form.getProgramme_id())
                    .ne("form_id",form.getForm_id()));
            if (otherForm != null){
                otherForm.setChose(0);
                int j = formMapper.updateById(otherForm);
                if (j == 1 && i == 1){
                    return Result.success("编辑成功");
                }
            }
            if (i == 1){
                return Result.success("编辑成功");
            }
            return Result.fail("编辑失败");
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
                form.setChose(1);
                formMapper.updateById(form);
                Programme programme = programmeMapper.selectById(form.getProgramme_id());
                programme.setDemand_capacity(form.getDemand_capacity());
                programme.setActual_capacity(form.getActual_capacity());
                programmeMapper.updateById(programme);

                Form unChose = formMapper.selectOne(new QueryWrapper<Form>()
                        .eq("programme_id",form.getProgramme_id())
                        .ne("form_id",form.getForm_id()));
                if (unChose != null){
                    unChose.setChose(0);
                    formMapper.updateById(unChose);
                }
                return Result.success();
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }


}
