package com.greek.shop.controller;

import com.greek.shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:10
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/code/{tel}")
    public void code(@PathVariable String tel) {
        authService.sendVerificationCode(tel);
    }

    @PostMapping("/login")
    public void login() {

    }

}
