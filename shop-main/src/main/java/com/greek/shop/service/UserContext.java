package com.greek.shop.service;

import com.greek.shop.entity.User;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/10/010 10:14
 */
public class UserContext {
    private static ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }
}
