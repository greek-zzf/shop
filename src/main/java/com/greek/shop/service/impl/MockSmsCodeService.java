package com.greek.shop.service.impl;

import com.greek.shop.service.SmsCodeService;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 模拟短信平台实现
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 17:51
 */
public class MockSmsCodeService implements SmsCodeService {
    @Override
    public String sendSmsCode(String tel) {
        return RandomStringUtils.randomNumeric(6);
    }
}
