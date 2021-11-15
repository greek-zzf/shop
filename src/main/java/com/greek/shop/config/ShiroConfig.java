package com.greek.shop.config;

import com.greek.shop.interceptor.ShiroLoginFilter;
import com.greek.shop.interceptor.UserLoginInterceptor;
import com.greek.shop.service.UserService;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/8/008 17:31
 */
@Configuration
public class ShiroConfig implements WebMvcConfigurer {

    @Autowired
    UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor(userService));
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> patten = new HashMap<>();
        patten.put("/api/code", "anon");
        patten.put("/api/login", "anon");
        patten.put("/api/status", "anon");
        patten.put("/api/logout", "anon");
        patten.put("/**", "shiroLoginFilter");

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("shiroLoginFilter", new ShiroLoginFilter());

        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(patten);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(AuthorizingRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }


}
