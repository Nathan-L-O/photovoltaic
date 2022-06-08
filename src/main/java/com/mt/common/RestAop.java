package com.mt.common;

import com.mt.request.UserBaseRequest;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * REST 切面
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:58
 */
public class RestAop {

    /**
     * 解析 HttpServletRequest
     *
     * @param joinPoint
     */
    protected HttpServletRequest parseHttpServletRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof HttpServletRequest) {
                return (HttpServletRequest) o;
            }
        }
        return null;
    }


    /**
     * 解析 BaseRestRequest
     *
     * @param joinPoint
     */
    protected BaseRestRequest parseRestRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof BaseRestRequest) {
                return (BaseRestRequest) o;
            }
        }
        return null;
    }

    /**
     * 解析 UserBaseRequest
     *
     * @param joinPoint
     */
    protected UserBaseRequest parseUserRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof UserBaseRequest) {
                return (UserBaseRequest) o;
            }
        }
        return null;
    }
}
