package com.greek.shop.api.enums;

/**
 * 状态枚举
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 16:17
 */
public enum StatusEnum {
    DELETE(),
    OK(),

    // Only for order
    PENDING(),
    PAID(),
    DELIVERED(),
    RECEIVED();



    public String getName() {
        return name().toLowerCase();
    }
}