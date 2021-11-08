package com.greek.shop.service;

import com.greek.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:13
 */
@Service
public class AuthService {
    private Map<String, String> telToCode;

    private final UserService userService;
    private final SmsCodeService smsCodeService;
    private final VerificationCodeCheckService verificationCodeCheckService;


    @Autowired
    public AuthService(UserService userService, SmsCodeService smsCodeService, VerificationCodeCheckService verificationCodeCheckService) {
        this.userService = userService;
        this.smsCodeService = smsCodeService;
        this.verificationCodeCheckService = verificationCodeCheckService;
    }

    public void sendVerificationCode(String tel) {
        User user = userService.createUserIfNotExist(tel);
        String correctCode = smsCodeService.sendSmsCode(tel);
        verificationCodeCheckService.addCode(tel, correctCode);
    }
}
