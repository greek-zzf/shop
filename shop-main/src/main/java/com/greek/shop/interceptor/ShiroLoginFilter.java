package com.greek.shop.interceptor;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/11/011 11:29
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * @return true-继续往下执行，false-该 filter 过滤器已经处理，不继续执行其他过滤器
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        return false;
    }
}
