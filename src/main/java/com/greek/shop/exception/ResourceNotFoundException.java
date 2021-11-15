package com.greek.shop.exception;

/**
 * 资源找不到，抛出该异常
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 16:10
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
