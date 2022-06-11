package com.mt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.common.annotation.LoginAuthentication;
import com.mt.mapper.FormMapper;
import com.mt.mapper.ProgrammeMapper;
import com.mt.pojo.Form;
import com.mt.pojo.Programme;
import com.mt.request.UserBaseRequest;
import com.mt.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/programme")
@Api(tags = "方案接口")
public class ProgrammeController {

    @Autowired
    private ProgrammeMapper programmeMapper;
    @Autowired
    private FormMapper formMapper;

    @ApiOperation(value="获取未删除的方案")
    @LoginAuthentication
    @RequestMapping(value = "selectNotDelete", method = RequestMethod.GET)
    public Result<Object> selectById(HttpServletRequest httpServletRequest, UserBaseRequest userBaseRequest,Boolean isCollcetion) {
        try {
            List<Programme> programmes = new ArrayList<>();
            if (isCollcetion){
                programmes = programmeMapper.selectList(new QueryWrapper<Programme>()
                        .eq("user_id",userBaseRequest.getUserId())
                        .eq("isCollection",1)
                        .ne("isDelete",1));
            }else {
                programmes = programmeMapper.selectList(new QueryWrapper<Programme>()
                        .eq("user_id",userBaseRequest.getUserId())
                        .ne("isDelete",1));
            }
            return Result.success(programmes);
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
                    .ne("isDelete",1));
            return Result.success(programmes);
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="创建方案")
    @LoginAuthentication
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result<Object> create(HttpServletRequest httpServletRequest,UserBaseRequest userBaseRequest,
            @RequestBody Programme programme,
            @RequestBody Form form) {
        try {
            int update = formMapper.updateById(form);
            if (update == 1){
                programme.setUser_id(userBaseRequest.getUserId());
                programme.setActual_capacity(form.getActual_capacity());
                programme.setDemand_capacity(form.getDemand_capacity());
                programme.setUpdate_date(new Date());
                int flag = programmeMapper.insert(programme);
                if (flag == 1){
                    return Result.success("创建成功");
                }
            }
            return Result.fail("创建失败");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="更新方案")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result<Object> update(
            @RequestBody List<Form> forms) {
        try {
            Boolean f = true;
            for (Form form : forms) {
                int flag = formMapper.updateById(form);
                if (flag != 1){
                    f = false;
                }
            }

            if (f){
                return Result.fail("更新成功");
            }
            return Result.fail("更新失败");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value="删除方案（删除到回收站)/移除回收站")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Result<Object> selectById(
            @ApiParam(value="方案id") Integer programme_id) {
        try {
            Programme programme = programmeMapper.selectById(programme_id);
            if (programme.getIsDelete() == 1){
                programme.setIsDelete(null);
            }else {
                programme.setIsDelete(1);
            }
            int flag = programmeMapper.updateById(programme);
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
            @ApiParam(value="方案id") Integer programme_id) {
        try {
            int flag = programmeMapper.deleteById(programme_id);
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
            @ApiParam(value="方案id") Integer programme_id) {
        try {
            Programme programme = programmeMapper.selectById(programme_id);
            if (programme.getIsCollection() == 1){
                programme.setIsCollection(null);
            }else {
                programme.setIsCollection(1);
            }
            int flag = programmeMapper.updateById(programme);
            if (flag == 1){
                return Result.success("操作成功");
            }
            return null;
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }


}
