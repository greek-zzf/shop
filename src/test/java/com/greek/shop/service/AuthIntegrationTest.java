package com.greek.shop.service;

import com.greek.shop.ShopApplication;
import com.greek.shop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static com.greek.shop.service.PhoneNumberValidatorTest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 16:35
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void returnHttpOkWhenParameterIsCorrect() throws Exception {
        postRequest("/api/code", null, VALID_PARAMETER, status().isOk());
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsNotCorrect() throws Exception {
        postRequest("/api/code", null, INVALID_PARAMETER, status().isBadRequest());
    }

    @Test
    public void loginLogoutTest() throws Exception {

        // 访问 /api/status 处于未登录状态
        MvcResult result = getRequest("/api/status", null, status().isOk());
        LoginResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        Assertions.assertFalse(content.isLogin());

        // 访问 /api/login 获取验证码，然后登录，判断是否登录成功
        postRequest("/api/code", null, VALID_PARAMETER, status().isOk());
        result = postRequest("/api/login", null,VALID_PARAMETER_CODE, status().isOk());
        Cookie sessionCookie = result.getResponse().getCookie("JSESSIONID");

        // 访问 /api/status 获取登录的用户信息
        result = getRequest("/api/status", sessionCookie, status().isOk());
        content = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        Assertions.assertTrue(content.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), content.getUser().getTel());

        // 访问 /api/logout 登出，判断是否登出成功
        postRequest("/api/logout", sessionCookie, "", status().isOk());

        // 访问 /api/status 处于未登录状态
        result = getRequest("/api/status", null, status().isOk());
        content = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        Assertions.assertFalse(content.isLogin());
    }

    @Test
    public void returnUnauthorizedNotLogin() throws Exception {
        postRequest("/api/any", null, "", status().isUnauthorized());
    }
}
