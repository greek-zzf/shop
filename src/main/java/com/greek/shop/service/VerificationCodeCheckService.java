package com.greek.shop.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码校验服务
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:18
 */
@Service
public class VerificationCodeCheckService {

    private Map<String, String> telToCode = new ConcurrentHashMap<>();

    public void addCode(String tel, String code) {
        telToCode.put(tel, code);
    }
}
