package com.mt.service;

import com.mt.pojo.user.User;
import com.mt.pojo.user.UserInfo;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 13:32
 */
public interface UserService {
    /**
     * 会话维持
     *
     * @param id
     * @return
     */
    User sessionMaintain(String id);

    /**
     * 会话维持
     *
     * @param request
     * @return
     */
    BasicUser sessionMaintain(UserBaseRequest request);

    /**
     * 用户登陆
     *
     * @param request
     * @return
     */
    BasicUser login(UserBaseRequest request);

    /**
     * 短信登录获取
     *
     * @param request
     * @return
     */
    void smsLoginRequest(UserBaseRequest request);

    /**
     * 短信登录执行
     *
     * @param request
     * @return
     */
    BasicUser smsLoginAction(UserBaseRequest request);

    /**
     * 旧密码重置密码
     */
    BasicUser reset(UserBaseRequest request);

    /**
     * 短信重置密码获取
     *
     * @param request
     * @return
     */
    void smsResetRequest(UserBaseRequest request);

    /**
     * 短信重置密码执行
     *
     * @param request
     * @return
     */
    BasicUser smsResetAction(UserBaseRequest request);

    /**
     * 注册短信验证码
     *
     * @param request
     * @return
     */
    void smsRegister(UserBaseRequest request);

    /**
     * 用户注册
     *
     * @param request
     * @return
     */
    void smsRegisterAction(UserBaseRequest request);

    /**
     * 头像上传
     */
    UserInfo uploadHeaderImage(MultipartFile headerImage, UserBaseRequest request);

    /**
     * 添加和更新用户信息
     */
    UserInfo addUserInfo(UserBaseRequest request);

    /**
     * 查看用户信息
     */
    UserInfo selectUserInfo(UserBaseRequest request);
}
