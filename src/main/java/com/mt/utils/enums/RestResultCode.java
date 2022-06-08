package com.mt.utils.enums;

import com.mt.utils.common.ResultCode;

/**
 * restful 结果码
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 16:08
 */
public enum RestResultCode implements ResultCode {

    /**
     * HTTP 200
     */
    SUCCESS("200", "执行成功"),

    /**
     * HTTP 206
     */
    PARTIAL_CONTENT("206", "部分执行"),

    /**
     * HTTP 400
     */
    ILLEGAL_PARAMETERS("400", "参数错误"),

    /**
     * HTTP 401
     */
    UNAUTHORIZED("401", "未授权访问"),

    /**
     * HTTP 403
     */
    FORBIDDEN("403", "拒绝访问"),

    /**
     * HTTP 404
     */
    NOT_FOUND("404", "无效资源"),

    /**
     * HTTP 405
     */
    METHOD_NOT_ALLOWED("405", "非法 HTTP 动词"),

    /**
     * HTTP 451
     */
    AUDIT_HIT("451", "由于触发审计规则, 您的请求被阻断"),

    /**
     * HTTP 500
     */
    SYSTEM_ERROR("500", "系统错误"),
    ;

    private final String code;

    private final String message;

    RestResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static RestResultCode getByCode(String code) {
        for (RestResultCode restResultCode : values()) {
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
