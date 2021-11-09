package com.greek.shop.service;

import com.greek.shop.dao.UserMapper;
import com.greek.shop.entity.User;
import com.greek.shop.entity.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:09
 */
@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User createUserIfNotExist(String tel) {
        User user = new User();
        user.setCreateAt(new Date());
        user.setUpdateAt(new Date());
        user.setTel(tel);
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            return getUserByTel(tel);
        }
        return user;
    }

    public User getUserByTel(String tel) {
        UserExample example = new UserExample();
        example.createCriteria().andTelEqualTo(tel);
        return userMapper.selectByExample(example).get(0);
    }
}
