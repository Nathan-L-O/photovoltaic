package com.mt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.mapper.FormMapper;
import com.mt.mapper.InverterMapper;
import com.mt.mapper.ProgrammeMapper;
import com.mt.mapper.StateMapper;
import com.mt.pojo.Inverter;
import com.mt.pojo.Programme;
import com.mt.pojo.State;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/state")
public class StateController {

    @Autowired
    private StateMapper stateMapper;
    @Autowired
    private ProgrammeMapper programmeMapper;

    //添加逆变器
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(
            @RequestBody State state) {
        try{
            List<State> states = stateMapper.selectList(new QueryWrapper<State>()
                    .eq("user_id",1)
                    .eq("state_name",state.getState_name()));
            if (states.size()>0){
                return Result.fail("状态名称重复");
            }
            state.setUser_id(1);
            stateMapper.insert(state);
            return Result.success(state);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    public Result getAll() {
        try{
            return Result.success(stateMapper.selectList(new QueryWrapper<State>()
                    .eq("user_id",0)
                    .or().eq("user_id",1)
                    ));
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result delete(@RequestBody State state) {
        try{
            stateMapper.deleteById(state.getState_id());
            Programme programme = new Programme();
            programme.setState_id(null);
            programmeMapper.update(programme,new QueryWrapper<Programme>().eq("programme_state",state.getState_id()));
            return Result.success("删除成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody State state) {
        try{
            List<State> states = stateMapper.selectList(new QueryWrapper<State>()
                    .eq("user_id",1)
                    .eq("state_name",state.getState_name()));
            if (states.size()>0){
                return Result.fail("状态名称重复");
            }
            stateMapper.updateById(state);
            return Result.success("更新成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }







}
