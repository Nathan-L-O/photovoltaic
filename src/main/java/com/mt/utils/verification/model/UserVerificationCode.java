package com.mt.utils.verification.model;

import com.mt.utils.AssertUtil;
import com.mt.utils.enums.CaptchaType;
import com.mt.utils.enums.CommonResultCode;
import com.mt.utils.verification.VerificationCodeUtil;

import java.util.List;

/**
 * 用户验证码模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/8 16:09
 */
public class UserVerificationCode {

    /**
     * UserID
     */
    private final String userId;

    /**
     * 验证码
     */
    private final String code;

    /**
     * 验证码校验体
     */
    private final String validateCode;

    /**
     * 时间戳
     */
    private final long timestamp;

    public UserVerificationCode(String userId, CaptchaType captchaType) {
        this.userId = userId;
        AssertUtil.assertTrue(captchaType.equals(CaptchaType.NUMBER), CommonResultCode.SYSTEM_ERROR);
        List<String> codeModel = VerificationCodeUtil.getNumCode();
        this.code = codeModel.get(0);
        this.validateCode = codeModel.get(0);
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public String getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
