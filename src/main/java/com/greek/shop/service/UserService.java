package com.greek.shop.service;

import com.greek.shop.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:09
 */
@Service
public class UserService {

    public User createUserIfNotExist(String tel) {
        return new User();
    }
}
