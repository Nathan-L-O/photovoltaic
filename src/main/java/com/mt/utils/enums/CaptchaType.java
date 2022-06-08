package com.mt.utils.enums;

import com.mt.utils.common.ResultCode;

/**
 * Captcha 类型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 16:08
 */
public enum CaptchaType implements ResultCode {

    /**
     * 数字验证码
     */
    NUMBER("NUMBER", "数字验证码");

    private final String code;

    private final String message;

    CaptchaType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CaptchaType getByCode(String code) {
        for (CaptchaType restResultCode : values()) {
            if (org.apache.commons.lang.StringUtils.equals(restResultCode.getCode(), code)) {
                return restResultCode;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
