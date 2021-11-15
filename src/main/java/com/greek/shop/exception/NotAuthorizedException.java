package com.greek.shop.exception;

/**
 * 权限不足时，抛出该异常
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 15:42
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}
