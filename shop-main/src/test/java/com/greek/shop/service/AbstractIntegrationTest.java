package com.greek.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greek.shop.entity.LoginResponse;
import com.greek.shop.generate.User;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;

import static com.greek.shop.service.PhoneNumberValidatorTest.VALID_PARAMETER;
import static com.greek.shop.service.PhoneNumberValidatorTest.VALID_PARAMETER_CODE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Zhaofeng Zhou
 * @date 2021/11/17/017 16:30
 */
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class AbstractIntegrationTest {

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    Environment environment;

    @BeforeEach
    void setUp() {
        // 每次运行测试的时候，都需要将给测试数据库灌数据
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, username, password);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    public CookieAndUser loginAndReturnCookie() throws Exception {
        // 访问 /api/status 处于未登录状态
        MvcResult result = getRequest("/api/v1/status", null, status().isOk());
        LoginResponse content = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        Assertions.assertFalse(content.isLogin());

        // 访问 /api/login 获取验证码，然后登录，判断是否登录成功
        postRequest("/api/v1/code", null, VALID_PARAMETER, status().isOk());
        result = postRequest("/api/v1/login", null, VALID_PARAMETER_CODE, status().isOk());

        Cookie sessionCookie = result.getResponse().getCookie("JSESSIONID");

        result = getRequest("/api/v1/status", sessionCookie, status().isOk());
        User user = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class).getUser();
        return new CookieAndUser(sessionCookie, user);
    }


    public MvcResult getRequest(String url, Cookie cookie, ResultMatcher resultMatcher) throws Exception {
        if (cookie == null) {
            cookie = new Cookie("test", "test");
        }
        return buildRequest(get(url).cookie(cookie), resultMatcher);
    }

    public MvcResult postRequest(String url, Cookie cookie, Object requestBody, ResultMatcher resultMatcher) throws Exception {
        if (cookie == null) {
            cookie = new Cookie("test", "test");
        }
        return buildRequest(post(url)
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)),
                resultMatcher);
    }

    public MvcResult deleteRequest(String url, Cookie cookie, ResultMatcher resultMatcher) throws Exception {
        if (cookie == null) {
            cookie = new Cookie("test", "test");
        }
        return buildRequest(delete(url).cookie(cookie), resultMatcher);
    }

    private MvcResult buildRequest(RequestBuilder builder, ResultMatcher resultMatcher) throws Exception {
        return mockMvc.perform(builder)
                .andExpect(resultMatcher)
                .andReturn();
    }

    public <T> T asJsonObject(MvcResult result, TypeReference<T> typeReference) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
    }

    public static class CookieAndUser {
        private Cookie cookie;
        private User user;

        public CookieAndUser(Cookie cookie, User user) {
            this.cookie = cookie;
            this.user = user;
        }

        public Cookie getCookie() {
            return cookie;
        }

        public User getUser() {
            return user;
        }
    }
}
