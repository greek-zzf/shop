package com.greek.shop.service;

/**
 * 短信服务接口
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 17:48
 */
public interface SmsCodeService {

    /**
     * 向一个指定的手机号发送验证码，返回正确验证码
     * @param tel 目标手机
     * @return 验证码
     */
    String sendSmsCode(String tel);
}
