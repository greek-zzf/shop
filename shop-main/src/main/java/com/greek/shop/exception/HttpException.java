package com.greek.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhaofeng Zhou
 * @date 16/11/2021 下午9:52
 */
@Transactional
public class HttpException extends RuntimeException {
    private int statusCode;

    private HttpException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public static HttpException notFound(String message) {
        return new HttpException(message, HttpStatus.NOT_FOUND.value());
    }

    public static HttpException forbidden(String message) {
        return new HttpException(message, HttpStatus.FORBIDDEN.value());
    }

    public static HttpException badRequest(String message) {
        return new HttpException(message, HttpStatus.BAD_REQUEST.value());
    }

    public static HttpException gone(String message) {
        return new HttpException(message, HttpStatus.GONE.value());
    }

    public int getStatusCode() {
        return statusCode;
    }
}
