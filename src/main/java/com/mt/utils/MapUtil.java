package com.mt.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Map 类型处理工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/8 16:10
 */
public class MapUtil {

    /**
     * 将 Map 携带参数转换为请求参数
     *
     * @param parameterMap
     * @return
     */
    public static String toUrlString(Map<String, String> parameterMap) {
        StringBuilder parameterBuffer = new StringBuilder();
        if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key, value;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (parameterMap.get(key) != null) {
                    value = parameterMap.get(key);
                } else {
                    value = "";
                }

                parameterBuffer.append(key).append("=").append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        return parameterBuffer.toString();
    }

}
