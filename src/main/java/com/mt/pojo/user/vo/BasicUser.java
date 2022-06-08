package com.mt.pojo.user.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mt.pojo.user.UserInfo;
import com.mt.utils.common.ToString;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础用户模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 10:05
 */
@Data
@TableName(value = "user")
@ApiModel(value = "user", description = "基础用户")
@EqualsAndHashCode(callSuper = true)
public class BasicUser extends ToString {

    private static final long serialVersionUID = -5508755166496807448L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 登陆凭证
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfoBO) {
        this.userInfo = userInfoBO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
