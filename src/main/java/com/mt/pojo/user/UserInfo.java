package com.mt.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mt.pojo.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 16:07
 */
@Data
@TableName(value = "user_info")
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseDO {

    private static final long serialVersionUID = 901406150463834204L;

    /**
     * 用户信息 ID
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "user_info_id")
    private String userInfoId;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号码
     */
    @TableField(value = "mobile_phone")
    private String mobilePhone;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 职位
     */
    private String job;

    /**
     * 昵称
     */
    private String nickname;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
