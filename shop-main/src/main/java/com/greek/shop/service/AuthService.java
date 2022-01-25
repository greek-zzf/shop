package com.greek.shop.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:13
 */
@Service
public class AuthService {

    private final UserService userService;
    private final SmsCodeService smsCodeService;
    private final VerificationCodeCheckService verificationCodeCheckService;
    private final SqlSessionFactory sqlSessionFactory;


    @Autowired
    public AuthService(UserService userService,
                       SmsCodeService smsCodeService,
                       VerificationCodeCheckService verificationCodeCheckService,
                       SqlSessionFactory sqlSessionFactory) {
        this.userService = userService;
        this.smsCodeService = smsCodeService;
        this.verificationCodeCheckService = verificationCodeCheckService;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void sendVerificationCode(String tel) {

        try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            userService.createUserIfNotExist(tel);
            String correctCode = smsCodeService.sendSmsCode(tel);
            verificationCodeCheckService.addCode(tel, correctCode);
            sqlSession.commit();
        }

    }
}
