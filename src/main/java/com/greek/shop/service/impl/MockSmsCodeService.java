package com.greek.shop.service.impl;

import com.greek.shop.service.SmsCodeService;
import org.springframework.stereotype.Service;

/**
 * 模拟短信平台实现
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 17:51
 */
@Service
public class MockSmsCodeService implements SmsCodeService {
    @Override
    public String sendSmsCode(String tel) {
        return "123456";
    }
}
