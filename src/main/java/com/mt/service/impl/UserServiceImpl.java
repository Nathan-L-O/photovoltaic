package com.mt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mt.mapper.UserInfoMapper;
import com.mt.mapper.UserMapper;
import com.mt.pojo.user.User;
import com.mt.pojo.user.UserInfo;
import com.mt.pojo.user.vo.BasicUser;
import com.mt.request.UserBaseRequest;
import com.mt.service.UserService;
import com.mt.utils.AssertUtil;
import com.mt.utils.HashUtil;
import com.mt.utils.UUIDUtil;
import com.mt.utils.enums.RestResultCode;
import com.mt.utils.verification.VerificationCodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 13:32
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String SESSION_ID = "sessionId";

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private VerificationCodeUtil verificationCodeUtil;

    @Override
    public User sessionMaintain(String token) {
        AssertUtil.assertStringNotBlank(token, RestResultCode.ILLEGAL_PARAMETERS);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SESSION_ID, HashUtil.md5(token));
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public BasicUser sessionMaintain(UserBaseRequest request) {
        BasicUser basicUser = new BasicUser();
        basicUser.setUserId(request.getUserId());
        try {
            basicUser.setUserInfo(userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("user_id", basicUser.getUserId())));
        } catch (Exception ignored) {
        }
        return basicUser;
    }

    @Override
    public BasicUser login(UserBaseRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));

        AssertUtil.assertNotNull(user, RestResultCode.UNAUTHORIZED, "用户名或密码错误");
        AssertUtil.assertEquals(Objects.requireNonNull(HashUtil.md5(HashUtil.md5(request.getPassword()) + user.getSalt())),
                user.getPassword(), RestResultCode.UNAUTHORIZED, "用户名或密码错误");

        return getBasicUser(user);
    }

    @Override
    public void smsLoginRequest(UserBaseRequest request) {
        AssertUtil.assertNotNull(userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername())),
                RestResultCode.NOT_FOUND, "用户不存在");
        verificationCodeUtil.initLoginSmsCode(request.getUsername());
    }

    @Override
    public BasicUser smsLoginAction(UserBaseRequest request) {
        VerificationCodeUtil.validate(request.getUsername(), request.getCode());
        AssertUtil.assertTrue(VerificationCodeUtil.validate(request.getUsername()), RestResultCode.UNAUTHORIZED, "验证过程非法");

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        VerificationCodeUtil.afterCaptcha(request.getUsername());

        return getBasicUser(user);
    }

    @Override
    public BasicUser reset(UserBaseRequest request) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        AssertUtil.assertNotNull(user, RestResultCode.NOT_FOUND, "用户或密码不正确");
        String newPassword = HashUtil.md5(HashUtil.md5(request.getNewPassword()) + user.getSalt());
        AssertUtil.assertEquals(newPassword,user.getPassword(),"用户或密码不正确");
        user.setPassword(newPassword);
        userMapper.insert(user);

        return getBasicUser(user);
    }

    @Override
    public void smsResetRequest(UserBaseRequest request) {
        AssertUtil.assertNotNull(userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername())),
                RestResultCode.NOT_FOUND, "用户不存在");
        verificationCodeUtil.initResetSmsCode(request.getUsername());
    }

    @Override
    public BasicUser smsResetAction(UserBaseRequest request) {
        VerificationCodeUtil.validate(request.getUsername(), request.getCode());
        AssertUtil.assertTrue(VerificationCodeUtil.validate(request.getUsername()), RestResultCode.UNAUTHORIZED, "验证过程非法");

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        user.setPassword(HashUtil.md5(HashUtil.md5(request.getNewPassword()) + user.getSalt()));
        userMapper.update(user, new QueryWrapper<User>().eq("user_id", user.getUserId()));

        VerificationCodeUtil.afterCaptcha(request.getUsername());

        return getBasicUser(user);
    }

    @Override
    public void smsRegister(UserBaseRequest request) {
        AssertUtil.assertNull(userMapper.selectOne(new QueryWrapper<User>().eq("username",request.getUsername())),"用户已存在");
        verificationCodeUtil.initRegisterSmsCode(request.getUsername());
    }

    @Override
    public void smsRegisterAction(UserBaseRequest request) {
        VerificationCodeUtil.validate(request.getUsername(),request.getCode());
        AssertUtil.assertTrue(VerificationCodeUtil.validate(request.getUsername()),RestResultCode.UNAUTHORIZED, "验证过程非法");

        User user = new User();
        String salt = UUIDUtil.generate(false);
        user.setUsername(request.getUsername());
        user.setPassword(HashUtil.md5(HashUtil.md5(request.getPassword()) + salt));
        user.setSalt(salt);
        userMapper.insert(user);

//        UserInfo userInfo = getUserInfo(user,request);
//        userInfoMapper.insert(userInfo);

        VerificationCodeUtil.afterCaptcha(request.getUsername());

    }

    private BasicUser getBasicUser(User user) {
        String token = UUIDUtil.generate(false);
        user.setSessionId(HashUtil.md5(token));
        userMapper.update(user, new QueryWrapper<User>().eq("user_id", user.getUserId()));

        BasicUser basicUser = new BasicUser();
        basicUser.setUserId(user.getUserId());
        basicUser.setToken(token);
        try {
            basicUser.setUserInfo(userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("user_id", user.getUserId())));
        } catch (Exception ignored) {
        }
        return basicUser;
    }

    /**
     * 根据用户名查询用户ID，从而添加到用户信息中，并将请求中的信息保存成用户信息中
     */
//    private UserInfo getUserInfo(User user,UserBaseRequest request){
//        UserInfo userInfo = new UserInfo();
//        user = userMapper.selectOne(new QueryWrapper<User>().eq("username",request.getUsername()));
//        userInfo.setUserId(user.getUserId());
//        userInfo.setRealName(request.getRealName());
//        userInfo.setSex(request.getSex());
//        userInfo.setMobilePhone(request.getUsername());
//        userInfo.setCompany(request.getCompany());
//        userInfo.setJob(request.getJob());
//        userInfo.setNickname(request.getNickname());
//        return userInfo;
//    }
}
