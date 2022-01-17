package com.greek.shop.enums;

/**
 * 状态枚举
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 16:17
 */
public enum StatusEnum {
    DELETE,
    OK;

    public String getName() {
        return name().toLowerCase();
    }
}
