package com.mt.controller;

import com.mt.common.annotation.LoginAuthentication;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;
import com.mt.service.UserService;
import com.mt.utils.AssertUtil;
import com.mt.utils.Result;
import com.mt.utils.enums.RestResultCode;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
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
    public Result<String> smsRequest(@RequestBody(required = false) UserBaseRequest request) {
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
        AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");

        return Result.success(userService.smsLoginAction(request));
    }

    /**
     * 重置密码请求
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/smsReset")
    public Result<String> smsResetRequest(@RequestBody(required = false) UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");

        userService.smsResetRequest(request);
        return Result.success("短信验证码已下发");
    }

    /**
     * 重置密码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/smsReset")
    public Result<BasicUser> smsReset(@RequestBody(required = false) UserBaseRequest request) {
        AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
        AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
        AssertUtil.assertStringNotBlank(request.getNewPassword(), RestResultCode.ILLEGAL_PARAMETERS, "新密码不能为空");

        return Result.success(userService.smsResetAction(request));
    }

}
