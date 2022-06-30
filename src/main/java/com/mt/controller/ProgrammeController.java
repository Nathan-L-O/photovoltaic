package com.mt.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mt.common.annotation.LoginAuthentication;
import com.mt.mapper.FormMapper;
import com.mt.mapper.InverterMapper;
import com.mt.mapper.ProgrammeMapper;
import com.mt.mapper.StateMapper;
import com.mt.pojo.Form;
import com.mt.pojo.Inverter;
import com.mt.pojo.Programme;
import com.mt.request.UserBaseRequest;
import com.mt.service.ProgrammeService;
import com.mt.utils.CalculationUtils;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/programme")
@Api(tags = "方案接口")
public class ProgrammeController {

    @Autowired
    private ProgrammeMapper programmeMapper;
    @Autowired
    private FormMapper formMapper;
    @Autowired
    private InverterMapper inverterMapper;
    @Autowired
    private StateMapper stateMapper;

    @Resource
    ProgrammeService programmeService;


    @ApiOperation(value="获取未删除的方案")
    @LoginAuthentication
    @RequestMapping(value = "selectNotDelete", method = RequestMethod.GET)
    public Result<Object> selectNotDelete(HttpServletRequest httpServletRequest, UserBaseRequest userBaseRequest) {
        try {
            List<Programme> programmes = new ArrayList<>();
            programmes = programmeMapper.selectList(new QueryWrapper<Programme>()
                       .eq("user_id",userBaseRequest.getUserId())
                    .eq("isDelete",0));
            return Result.success(selectState(programmes));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
    @ApiOperation(value="获取删除的方案")
    @LoginAuthentication
    @RequestMapping(value = "selectDelete", method = RequestMethod.GET)
    public Result<Object> selectDelete(HttpServletRequest httpServletRequest, UserBaseRequest userBaseRequest) {
        try {
            List<Programme> programmes = new ArrayList<>();
            programmes = programmeMapper.selectList(new QueryWrapper<Programme>()
                       .eq("user_id",userBaseRequest.getUserId())
                    .eq("isDelete",1));
            return Result.success(selectState(programmes));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }


    @ApiOperation(value="获取收藏的方案")
    @LoginAuthentication
    @RequestMapping(value = "selectIsCollection", method = RequestMethod.GET)
    public Result<Object> selectIsCollection(HttpServletRequest httpServletRequest, UserBaseRequest userBaseRequest) {
        try {
            List<Programme> programmes = programmeMapper.selectList(new QueryWrapper<Programme>()
                    .eq("user_id",userBaseRequest.getUserId())
                    .eq("isDelete",0)
                    .eq("isCollection",1));
            return Result.success(selectState(programmes));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }
    @ApiOperation(value="获取方案详情")
//    @LoginAuthentication
    @RequestMapping(value = "selectProgrammeDetails", method = RequestMethod.GET)
    public Result<Object> selectProgramme(HttpServletRequest httpServletRequest, UserBaseRequest userBaseRequest, Integer programme_id) {
        try {


            return Result.success(programmeService.selectProgrammeDetails(programme_id));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }


    @ApiOperation(value="创建方案")
    @LoginAuthentication
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result<Object> create(HttpServletRequest httpServletRequest,UserBaseRequest userBaseRequest,
            @RequestBody Programme programme) {
        try{
            List<Form> list = new ArrayList<>();

            programme.setUser_id(userBaseRequest.getUserId());
            programme.setUpdate_date(new Date());
            programme.setIsCollection(0);
            programme.setIsDelete(0);
            programme.setCreate_date(new Date());
            int flag = programmeMapper.insert(programme);
            if (1 == flag) {
                Inverter inverter = inverterMapper.selectById(String.valueOf(programme.getInverter_id()));
                Map<String, Form> map = CalculationUtils.getGeneratingCapacity(inverter, programme.getDemand_capacity(), Integer.valueOf(programme.getInverter_num()));

                for (Form form : map.values()) {
                    form.setProgramme_id(programme.getProgramme_id());
                    formMapper.insert(form);
                    list.add(form);
                }

                if (list.size() == 2) {
                    //输出的第一个为实际发电量-需求发电量最小的方案
                    Collections.sort(list, Comparator.comparingInt(o -> (int) (Double.parseDouble(o.getDemand_capacity()) - Double.parseDouble(o.getActual_capacity()))));
                    //如果第一个方案超出逆变器电压范围则将第二个方案设置为最优
                    if (list.get(0).getErrmsg() != null && list.get(1).getErrmsg() == null) {
                        Form form = list.get(1);
                        list.set(1, list.get(0));
                        list.set(0, form);
                    }
                }
            }
            return Result.success(list);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="更新方案")
    @LoginAuthentication
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result<Object> update(
            @RequestBody Programme programme) {
        try{
            programme.setUpdate_date(new Date());
            int flag = programmeMapper.updateById(programme);
            if (flag ==1){
                Inverter inverter = inverterMapper.selectById(String.valueOf(programme.getInverter_id()));
                Map<String,Form> map = CalculationUtils.getGeneratingCapacity(inverter,programme.getDemand_capacity(),Integer.valueOf(programme.getInverter_num()));
                List<Form> list = new ArrayList<>();
                formMapper.delete(new QueryWrapper<Form>().eq("programme_id",programme.getProgramme_id()));
                if (map.get("practical")!=null){
                    Form form = map.get("practical");
                    form.setProgramme_id(programme.getProgramme_id());
                    formMapper.insert(form);
                    list.add(form);
                }
                if (map.get("economic")!=null){
                    Form form = map.get("economic");
                    form.setProgramme_id(programme.getProgramme_id());
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

            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除方案（删除到回收站)/移出回收站")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result<Object> delete(
            @RequestBody Programme programme) {
        try {
            Programme program = programmeMapper.selectById(programme.getProgramme_id());
            if (program.getIsDelete() == 1){
                program.setIsDelete(0);
            }else {
                program.setIsDelete(1);
            }
            int flag = programmeMapper.updateById(program);
            if (flag == 1){
                return Result.success("操作成功");
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除方案（真删除")
    @RequestMapping(value = "thoroughDelete", method = RequestMethod.POST)
    public Result<Object> thoroughDelete(
            @RequestBody Programme programme) {
        try {
            int flag = programmeMapper.deleteById(programme.getProgramme_id());
            if (flag == 1){
                return Result.success("删除成功");
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="收藏/取消收藏")
    @RequestMapping(value = "collection", method = RequestMethod.POST)
    public Result<Object> collection(
            @RequestBody Programme programme) {
        try {
            Programme program = programmeMapper.selectById(programme.getProgramme_id());
            if (program.getIsCollection() == 1){
                program.setIsCollection(0);
            }else {
                program.setIsCollection(1);
            }
            int flag = programmeMapper.updateById(program);
            if (flag == 1){
                return Result.success("操作成功");
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="修改方案状态")
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public Result<Object> updateState(
            @RequestBody Programme programme) {
        try {
            programmeMapper.update(null,new UpdateWrapper<Programme>().set("state_id",programme.getState_id()).eq("programme_id",programme.getProgramme_id()));
            return Result.success("更新成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="复制")
    @RequestMapping(value = "copy", method = RequestMethod.POST)
    public Result<Object> copy(
            @RequestBody Programme programme) {
        try {
            Programme program = programmeMapper.selectById(programme.getProgramme_id());
            List<Form> forms = formMapper.selectList(new QueryWrapper<Form>().eq("programme_id",program.getProgramme_id()));
            program.setProgramme_id(null);
            program.setUpdate_date(new Date());
            program.setCreate_date(new Date());
            program.setProgramme_name(programme.getProgramme_name()+"的副本");
            int p = programmeMapper.insert(program);
            Boolean flag = true;
            for (Form form : forms) {
                form.setForm_id(null);
                form.setProgramme_id(program.getProgramme_id());
                int f = formMapper.insert(form);
                if (f != 1)
                    flag = false;
            }
            if (p == 1 && flag){
                return Result.success("复制成功");
            }
            return Result.fail("复制失败");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    public List<Programme> selectState(List<Programme> programmes){
        for (Programme p:programmes) {
            if (p.getState_id() != null){
                p.setState(stateMapper.selectById(p.getState_id()));
            }
        }
        return programmes;
    }
}
