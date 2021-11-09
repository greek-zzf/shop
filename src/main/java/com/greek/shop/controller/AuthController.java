package com.greek.shop.controller;

import com.greek.shop.service.AuthService;
import com.greek.shop.validator.annotaion.Phone;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:10
 */
@RestController
@Validated
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/code")
    public void code(@Valid @RequestBody TelAndCode telAndCode) {
        authService.sendVerificationCode(telAndCode.getTel());
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(telAndCode.getTel(), telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);
    }

    public static class TelAndCode {

        @NotBlank(message = "手机号码不能为空！")
        @Phone
        private String tel;
        private String code;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}
