package com.mt.common.ascept;

import com.mt.common.BaseRestRequest;
import com.mt.common.RestAop;
import com.mt.pojo.user.User;
import com.mt.service.UserService;
import com.mt.utils.AssertUtil;
import com.mt.utils.enums.RestResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 切面会话服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:49
 */
@Order(1)
@Aspect
@Component
public class SessionService extends RestAop {

    /**
     * HTTP 请求头 token
     */
    private static final String TOKEN = "Authorization";

    @Resource
    UserService userService;

    @Pointcut("execution(* com.mt.controller..*(..)) && @annotation(com.mt.common.annotation.LoginAuthentication)")
    public void token() {
    }

    /**
     * 登陆态校验, 增强
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "token()")
    public Object verifyToken(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = parseHttpServletRequest(proceedingJoinPoint);
        AssertUtil.assertNotNull(httpServletRequest, RestResultCode.SYSTEM_ERROR, "HTTP 请求解析失败");
        BaseRestRequest request = parseRestRequest(proceedingJoinPoint);

        String token = httpServletRequest.getHeader(TOKEN);
        AssertUtil.assertStringNotBlank(token, RestResultCode.ILLEGAL_PARAMETERS, "登陆凭证缺失");
        User user = userService.sessionMaintain(token);
        AssertUtil.assertNotNull(user, RestResultCode.UNAUTHORIZED, "登陆凭证无效或已过期");

        if (request != null) {
            request.setUserId(user.getUserId());
        }

        return proceedingJoinPoint.proceed();
    }

}
