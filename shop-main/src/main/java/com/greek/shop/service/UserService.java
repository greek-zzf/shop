package com.greek.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greek.shop.dao.UserMapper;
import com.greek.shop.entity.User;
import com.greek.shop.entity.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 18:09
 */
@Service
public class UserService {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_KEY = "TEL_TO_USER";

    private final UserMapper userMapper;
    private final Jedis jedis;

    @Autowired
    public UserService(UserMapper userMapper, Jedis jedis) {
        this.userMapper = userMapper;
        this.jedis = jedis;
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

    public User getUserByTelFromDatabase(String tel) {
        UserExample example = new UserExample();
        example.createCriteria().andTelEqualTo(tel);

        User result = userMapper.selectByExample(example).get(0);
        try {
            jedis.hset(USER_KEY, tel, objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public User getUserByTel(String tel) {
        String userInfoStr = jedis.hget(USER_KEY, tel);
        if (StringUtils.isEmpty(userInfoStr)) {
            return getUserByTelFromDatabase(tel);
        }

        try {
            return objectMapper.readValue(userInfoStr, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
