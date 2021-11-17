package com.greek.shop.service;

import com.greek.shop.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/9/009 15:23
 */
@SpringBootTest
public class PhoneNumberValidatorTest extends AbstractIntegrationTest {
    public static AuthController.TelAndCode VALID_PARAMETER = new AuthController.TelAndCode("13134070272", null);
    public static AuthController.TelAndCode INVALID_PARAMETER = new AuthController.TelAndCode("1313407027", null);
    public static AuthController.TelAndCode NULL_PARAMETER = new AuthController.TelAndCode(null, null);
    public static AuthController.TelAndCode VALID_PARAMETER_CODE = new AuthController.TelAndCode("13134070272", "123456");


    @Test
    public void TelIsNull() throws Exception {
        postRequest("/api/code", null, NULL_PARAMETER, status().isBadRequest());
    }

}
