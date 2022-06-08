package com.mt.utils;

import com.mt.exception.MengTuException;
import com.mt.utils.common.ResultCode;
import com.mt.utils.enums.CommonResultCode;
import org.apache.commons.lang.StringUtils;

/**
 * 断言工具类(精简至仅需 @ 2022.06.08)
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:15
 */
public class AssertUtil {

    /**
     * 断言真表达式
     *
     * @param expression
     */
    public static void assertTrue(Boolean expression) {
        if (expression == null || !expression) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertTrue(Boolean expression, String errorMsg) {
        if (expression == null || !expression) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode) {
        if (expression == null || !expression) {
            throw new MengTuException(resultCode);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode, String errorMsg) {
        if (expression == null || !expression) {
            throw new MengTuException(resultCode, errorMsg);
        }
    }

    /**
     * 断言空对象
     *
     * @param obj
     * @param errorMsg
     */
    public static void assertNull(Object obj, String errorMsg) {
        if (obj != null) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    /**
     * 断言非空对象
     *
     * @param obj
     */
    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertNotNull(Object obj, String errorMsg) {
        assertNotNull(obj, CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
    }

    public static void assertNotNull(Object obj, ResultCode resultCode, String errorMsg) {
        if (obj == null) {
            throw new MengTuException(resultCode, errorMsg);
        }
    }

    public static void assertNotNull(Object obj, ResultCode resultCode) {
        if (obj == null) {
            throw new MengTuException(resultCode);
        }
    }

    /**
     * 断言非空字符串
     *
     * @param str
     * @param errorMsg
     */
    public static void assertStringNotBlank(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertStringNotBlank(String str, ResultCode resultCode, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new MengTuException(resultCode, errorMsg);
        }
    }

    public static void assertStringNotBlank(String str, ResultCode resultCode) {
        if (StringUtils.isBlank(str)) {
            throw new MengTuException(resultCode);
        }
    }

    public static void assertStringNotBlank(String str) {
        if (StringUtils.isBlank(str)) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertStringBlank(String str, String errorMsg) {
        if (StringUtils.isNotBlank(str)) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    /**
     * 断言对象相等
     *
     * @param o1
     * @param o2
     * @param errorMsg
     */
    public static void assertEquals(Object o1, Object o2, String errorMsg) {
        if (!o1.equals(o2)) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertEquals(Object o1, Object o2, ResultCode resultCode) {
        if (!o1.equals(o2)) {
            throw new MengTuException(resultCode);
        }
    }

    public static void assertEquals(Object o1, Object o2, ResultCode resultCode, String errorMsg) {
        if (!o1.equals(o2)) {
            throw new MengTuException(resultCode.getCode(), errorMsg);
        }
    }

    public static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new MengTuException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

}
