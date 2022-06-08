package com.mt.utils.enums;

import com.mt.utils.common.ResultCode;
import org.apache.commons.lang.StringUtils;

/**
 * 通用结果码
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 16:08
 */
public enum CommonResultCode implements ResultCode {

    /**
     * 系统异常
     */
    SYSTEM_ERROR("系统异常"),

    /**
     * 参数异常
     */
    ILLEGAL_PARAMETERS("参数异常"),

    /**
     * 无权访问(未登录)
     */
    UNAUTHORIZED("未登录无权访问"),

    /**
     * 无权访问(无权限)
     */
    FORBIDDEN("无权访问"),

    /**
     * 调用成功
     */
    SUCCESS("调用成功");

    private String message;

    public static CommonResultCode getByCode(String code) {
        for (CommonResultCode commonResultCode : values()) {
            if (StringUtils.equals(commonResultCode.getCode(), code)) {
                return commonResultCode;
            }
        }
        return null;
    }

    CommonResultCode(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

}
