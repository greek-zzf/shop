package com.greek.shop.global;

import com.greek.shop.entity.LoginResponse;
import com.greek.shop.api.data.Page;
import com.greek.shop.entity.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/15/015 14:52
 */
@RestControllerAdvice(basePackages = "com.greek.shop.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return needConvert(body) ? Result.success(body) : body;
    }

    private boolean needConvert(Object object) {
        return !(object instanceof Result || object instanceof LoginResponse || object instanceof Page);
    }
}
