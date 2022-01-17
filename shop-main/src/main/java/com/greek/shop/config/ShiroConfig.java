package com.greek.shop.config;

import com.greek.shop.interceptor.ShiroLoginFilter;
import com.greek.shop.interceptor.UserLoginInterceptor;
import com.greek.shop.service.UserService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Autowired
    private UserService userService;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor(userService));
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> patten = new HashMap<>();
        patten.put("/api/v1/code", "anon");
        patten.put("/api/v1/login", "anon");
        patten.put("/api/v1/status", "anon");
        patten.put("/api/v1/logout", "anon");
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
        securityManager.setCacheManager(redisCacheManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }

    @Bean
    public CacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisHost + ":" + redisPort);
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }


    @Bean
    public Jedis getJedis() {
        JedisPool jedisPool = new JedisPool(redisHost, Integer.parseInt(redisPort));
        return jedisPool.getResource();
    }
}
