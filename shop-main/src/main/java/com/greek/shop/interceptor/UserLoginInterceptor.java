package com.greek.shop.interceptor;

import com.greek.shop.generate.User;
import com.greek.shop.service.UserContext;
import com.greek.shop.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 登录拦截器
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/10/010 10:17
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Autowired
    public UserLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object tel = SecurityUtils.getSubject().getPrincipal();
        if (Objects.nonNull(tel)) {
            User user = userService.getUserByTel(tel.toString());
            UserContext.setCurrentUser(user);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 由于线程会复用，因此处理完请求后应该清理信息。防止出现「串号」
        UserContext.setCurrentUser(null);
    }
}
