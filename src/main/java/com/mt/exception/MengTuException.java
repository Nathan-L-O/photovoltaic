package com.mt.exception;

import com.mt.utils.common.ResultCode;
import com.mt.utils.enums.CommonResultCode;

/**
 * 通用业务异常
 *
 * @author 过昊天 @ 2022/4/21 14:19
 * @version 1.0
 */
public class MengTuException extends RuntimeException {

    private static final long serialVersionUID = -4559736423824194476L;

    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 构造器
     */
    public MengTuException(final Throwable cause) {
        super(cause);
        this.errorCode = CommonResultCode.SYSTEM_ERROR.getCode();
        this.message = CommonResultCode.SYSTEM_ERROR.getMessage();
    }

    public MengTuException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public MengTuException(ResultCode resultCode) {
        this.errorCode = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public MengTuException(ResultCode resultCode, String message) {
        this.errorCode = resultCode.getCode();
        this.message = message;
    }

    public MengTuException(Throwable cause, String errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public MengTuException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 异常获取信息，重载
     */
    @Override
    public String getMessage() {
        return message;
    }
}
