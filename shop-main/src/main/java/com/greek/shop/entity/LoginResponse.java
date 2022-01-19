package com.greek.shop.entity;

import com.greek.shop.generate.User;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/10/010 11:02
 */
public class LoginResponse {
    private boolean login;
    private User user;

    public LoginResponse() {
    }

    public static LoginResponse notLogin() {
        return new LoginResponse(false, null);
    }

    public static LoginResponse login(User user) {
        return new LoginResponse(true, user);
    }

    private LoginResponse(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

