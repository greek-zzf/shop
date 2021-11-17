package com.greek.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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


    public MvcResult getRequest(String url, Cookie cookie, ResultMatcher resultMatcher) throws Exception {
        if (cookie == null) {
            cookie = new Cookie("test", "test");
        }
        return mockMvc.perform(get(url)
                .cookie(cookie))
                .andExpect(resultMatcher)
                .andReturn();
    }

    public MvcResult postRequest(String url, Cookie cookie, Object requestBody, ResultMatcher resultMatcher) throws Exception {
        if (cookie == null) {
            cookie = new Cookie("test", "test");
        }
        return mockMvc.perform(post(url)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(resultMatcher)
                .andReturn();
    }
}
