package com.mt.utils;

import java.util.UUID;

/**
 * 通用唯一识别码生成工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:05
 */
public class UUIDUtil {

    /**
     * 随机生成 UUID
     *
     * @param hasDashes 是否保留短划线
     * @return
     */
    public static String generate(Boolean hasDashes) {
        String uuid = UUID.randomUUID().toString();
        return hasDashes ? uuid : uuid.replace("-", "");
    }

}
