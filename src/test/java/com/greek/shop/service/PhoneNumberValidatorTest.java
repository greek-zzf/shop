package com.greek.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greek.shop.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 15:23
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PhoneNumberValidatorTest {
    public static AuthController.TelAndCode VALID_PARAMETER = new AuthController.TelAndCode("13134070272", null);
    public static AuthController.TelAndCode INVALID_PARAMETER = new AuthController.TelAndCode("1313407027", null);
    public static AuthController.TelAndCode NULL_PARAMETER = new AuthController.TelAndCode(null, null);
    public static AuthController.TelAndCode VALID_PARAMETER_CODE = new AuthController.TelAndCode("13134070272", "123456");

    @Autowired
    private MockMvc mvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void TelIsNull() throws Exception {
        mvc.perform(post("/api/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(NULL_PARAMETER)))
                .andExpect(status().isBadRequest());
    }

}
