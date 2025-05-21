package com.liteGrass.mq.springboot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @Description 测试消息
 * @Author liteGrass
 * @Date 2024/12/5 19:42
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootBaseProduct {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testMethod1() throws Exception {
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/mq/asyncSendNormalMessage"));
        System.out.println(perform.andReturn().getResponse());
    }

}
