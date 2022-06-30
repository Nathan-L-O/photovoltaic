package com.mt.pojo.user;

import com.baomidou.mybatisplus.annotation.*;
import com.mt.pojo.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户
 *
 * @version 1.0 @ 2022/6/7 16:07
 */
@Data
@TableName(value = "user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDO {

    private static final long serialVersionUID = -2891618189116921767L;

    /**
     * userId 生成 不可修改
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "user_id")
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码 长度不超过32位
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * sessionId
     */
    private String sessionId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

