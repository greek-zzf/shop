package com.greek.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:13
 */
@Service
public class AuthService {

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
        userService.createUserIfNotExist(tel);
        String correctCode = smsCodeService.sendSmsCode(tel);
        verificationCodeCheckService.addCode(tel, correctCode);
    }
}
