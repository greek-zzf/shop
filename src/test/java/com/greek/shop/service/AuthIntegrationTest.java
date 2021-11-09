package com.greek.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greek.shop.ShopApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 16:35
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    Environment environment;
    @Autowired
    MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void returnHttpOkWhenParameterIsCorrect() throws Exception {
        mockMvc.perform(post("/api/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PhoneNumberValidatorTest.VALID_PARAMETER)))
                .andExpect(status().isOk());
    }

    @Test
    public void returnHttpBadRequestWhenParameterIsNotCorrect() throws Exception {
        mockMvc.perform(post("/api/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PhoneNumberValidatorTest.INVALID_PARAMETER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginLogoutTest() throws Exception {
        // steps:
        // 访问 /api/status 处于未登录状态
        // 访问 /api/login 登录，判断是否登录成功
        // 访问 /api/status 获取登录的用户信息
        // 访问 /api/logout 登出，判断是否登出成功
        // 访问 /api/status 处于未登录状态
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PhoneNumberValidatorTest.VALID_PARAMETER)))
                .andExpect(status().isOk());
        // .andExpect(result -> result.get)
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isUnauthorized());


    }
}
