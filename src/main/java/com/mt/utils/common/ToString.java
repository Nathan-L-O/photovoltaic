package com.mt.utils.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


/**
 * 反射构建 ToString
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 09:19
 */
public class ToString implements Serializable {

    private static final long serialVersionUID = -927864564805882733L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
