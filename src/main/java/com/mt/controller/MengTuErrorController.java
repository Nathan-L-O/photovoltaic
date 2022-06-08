package com.mt.controller;

import com.mt.exception.MengTuException;
import com.mt.utils.Result;
import com.mt.utils.enums.RestResultCode;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 接管 ErrorController
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:30
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/error", produces = {"application/json;charset=UTF-8"})
public class MengTuErrorController implements ErrorController {

    /**
     * 错误信息
     */
    private static final String ERROR_MESSAGE = "message";

    @Resource
    private ErrorAttributes errorAttributes;

    @RequestMapping
    public Result<RestResultCode> restErrorReturn(HttpServletRequest request, HttpServletResponse response) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);

        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.BINDING_ERRORS,
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.STACK_TRACE);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, errorAttributeOptions);

        //TODO: (过昊天 @ 2022.06.07) 未知原因导致抛出的自定义错误均返回 500 Internal Server Error
        // 临时解决: 直接取原异常解析，覆盖错误码
        // 检查: SpringBoot2.x.x 版本问题
        // 检查: 错误类重载方法
        Throwable exceptionInstance = this.errorAttributes.getError(servletWebRequest);
        RestResultCode result;
        try {
            result = Objects.requireNonNull(RestResultCode.getByCode(String.valueOf(((MengTuException) exceptionInstance).getErrorCode())));
        } catch (Exception e) {
            result = Objects.requireNonNull(RestResultCode.getByCode(String.valueOf(errorAttributes.get("status"))));
        }

        response.setStatus(Integer.parseInt(RestResultCode.SUCCESS.getCode()));

        return Result.build(result.getCode().equals(RestResultCode.SUCCESS.getCode()),
                Integer.parseInt(result.getCode()), String.valueOf(errorAttributes.get(ERROR_MESSAGE)));
    }

}
