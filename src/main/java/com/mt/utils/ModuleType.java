package com.mt.utils;

import io.swagger.models.auth.In;

public enum ModuleType {
     MODULE_TYPE_0_5C(12,19),
    MODULE_TYPE_1C(24, 10);

    //验证上下限公式中的值
    private int name;

    //数量上限
    private int index;

    // 构造方法

    private ModuleType(Integer name, int index) {

        this.name = name;

        this.index = index;

    }

    public static Integer getName(int index) {

        for (ModuleType moduleType: ModuleType.values()) {

            if (moduleType.getIndex() == index) {

                return moduleType.name;

            }

        }

        return null;

    }

    public Integer getName() {

        return name;

    }

    public void setName(Integer name) {

        this.name = name;

    }

    public int getIndex() {

        return index;

    }

    public void setIndex(int index) {

        this.index = index;

    }
}
