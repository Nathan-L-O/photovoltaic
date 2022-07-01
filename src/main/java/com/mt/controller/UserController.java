package com.mt.controller;

import com.mt.common.annotation.LoginAuthentication;
import com.mt.exception.MengTuException;
import com.mt.pojo.user.UserInfo;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;
import com.mt.service.UserService;
import com.mt.utils.AssertUtil;
import com.mt.utils.Result;
import com.mt.utils.enums.RestResultCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/8 15:06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 会话维持
     *
     * @param httpServletRequest
     * @param request
     * @return
     */
    @GetMapping
    @LoginAuthentication
    public Result<BasicUser> login(HttpServletRequest httpServletRequest, UserBaseRequest request) {
        return Result.success(userService.sessionMaintain(request));
    }

    /**
     * 通用登录
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public Result<BasicUser> login(@RequestBody(required = false) UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
        AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "密码不能为空");

        return Result.success(userService.login(request));
    }

    /**
     * 短信验证码登录请求
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/smsLogin")
    public Result<String> smsRequest(UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");

        userService.smsLoginRequest(request);
        return Result.success("短信验证码已下发");
    }

    /**
     * 短信验证码登录
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/smsLogin")
    public Result<BasicUser> smsLogin(@RequestBody(required = false) UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
        AssertUtil.assertStringNotBlank(request.getCode(), RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");

        return Result.success(userService.smsLoginAction(request));
    }
    /**
     * 旧密码重置密码
     *
     * @param request
     * @return
     */
//    @PostMapping(value = "/reset")
//    public Result<BasicUser> reset(@RequestBody(required = false) UserBaseRequest request){
//        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
//        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
//        AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "旧密码不能为空");
//        AssertUtil.assertStringNotBlank(request.getNewPassword(), RestResultCode.ILLEGAL_PARAMETERS, "新密码不能为空");
//
//        return Result.success(userService.reset(request));
//    }

    /**
     * 验证码重置密码请求
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/smsReset")
    public Result<String> smsResetRequest(UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");

        userService.smsResetRequest(request);
        return Result.success("短信验证码已下发");
    }

    /**
     * 验证码重置密码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/smsReset")
    public Result<BasicUser> smsReset(@RequestBody(required = false) UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
        AssertUtil.assertStringNotBlank(request.getCode(), RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
        AssertUtil.assertStringNotBlank(request.getNewPassword(), RestResultCode.ILLEGAL_PARAMETERS, "新密码不能为空");

        return Result.success(userService.smsResetAction(request));
    }
    /**
     * 短信验证码注册请求
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/smsRegister")
    public Result<String> smsRegisterRequest(UserBaseRequest request){
        AssertUtil.assertNotNull(request,RestResultCode.ILLEGAL_PARAMETERS);

        userService.smsRegister(request);
        return Result.success("短信验证码已下发");
    }

    /**
     * 注册
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/smsRegister")
    public Result<String> smsRegister(@RequestBody(required = false) UserBaseRequest request){
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
        AssertUtil.assertStringNotBlank(request.getCode(), RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
        AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "密码不能为空");

        userService.smsRegisterAction(request);
        return Result.success("注册成功");
    }

    /**
     * 头像上传
     */
    @PostMapping(value = "/uploadHeaderImage")
    @LoginAuthentication
    public Result<BasicUser> uploadHeaderImage(@RequestParam("headerImage") MultipartFile headerImage, HttpServletRequest httpServletRequest, UserBaseRequest request){
        AssertUtil.assertNotNull(headerImage,"图片未上传");

        BasicUser basicUser = new BasicUser();
        UserInfo userInfo = userService.uploadHeaderImage(headerImage,request);
        basicUser.setUserId(userInfo.getUserId());
        basicUser.setUserInfo(userInfo);
        basicUser.setToken(httpServletRequest.getHeader("Authorization"));
        return Result.success(basicUser);
    }

//    /**
//     * 添加用户信息
//     */
//    @PostMapping(value = "/addUserInfo")
//    @LoginAuthentication
//    public Result<BasicUser> addUserInfo(@RequestBody UserBaseRequest request,HttpServletRequest httpServletRequest){
//        BasicUser basicUser = new BasicUser();
//        UserInfo userInfo = userService.addUserInfo(request);
//        basicUser.setUserId(userInfo.getUserId());
//        basicUser.setUserInfo(userInfo);
//        basicUser.setToken(httpServletRequest.getHeader("Authorization"));
//        return Result.success(basicUser);
//    }

    /**
     * 修改用户信息
     */
    @PostMapping(value = "/updateUserInfo")
    @LoginAuthentication
    public Result<BasicUser> updateUserInfo(@RequestBody UserBaseRequest request,HttpServletRequest httpServletRequest){
        AssertUtil.assertStringNotBlank(request.getRealName(),RestResultCode.ILLEGAL_PARAMETERS,"真实姓名不能为空");
        AssertUtil.assertStringNotBlank(request.getCompany(),RestResultCode.ILLEGAL_PARAMETERS,"公司名称不能为空");
        AssertUtil.assertStringNotBlank(request.getJob(),RestResultCode.ILLEGAL_PARAMETERS,"职称不能为空");

        BasicUser basicUser = new BasicUser();
        UserInfo userInfo = userService.addUserInfo(request);
        basicUser.setUserId(userInfo.getUserId());
        basicUser.setUserInfo(userInfo);
        basicUser.setToken(httpServletRequest.getHeader("Authorization"));
        return Result.success(basicUser);
    }
    /**
     * 查看用户信息
     */
    @GetMapping(value = "/selectUserInfo")
    @LoginAuthentication
    public Result<BasicUser> selectUserInfo(UserBaseRequest request,HttpServletRequest httpServletRequest){
        BasicUser basicUser = new BasicUser();
        UserInfo userInfo = userService.selectUserInfo(request);
        basicUser.setUserId(userInfo.getUserId());
        basicUser.setUserInfo(userInfo);
        basicUser.setToken(httpServletRequest.getHeader("Authorization"));
        return Result.success(basicUser);
    }
}
