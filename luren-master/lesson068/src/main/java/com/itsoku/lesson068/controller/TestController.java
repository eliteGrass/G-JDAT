package com.itsoku.lesson068.controller;

import cn.hutool.crypto.symmetric.AES;
import com.itsoku.lesson068.secure.Decrypt;
import com.itsoku.lesson068.secure.Encrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/3 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class TestController {

    /**
     * 参数加密测试，需要在方法上标注 @Decrypt 注解
     *
     * @param body
     * @return
     */
    @Decrypt
    @PostMapping("/decryptTest")
    public List<String> decryptTest(@RequestBody List<String> body) {
        log.info("参数加密测试，请求参数：{}", body);
        return body;
    }

    /**
     * 返回值加密测试，需要在方法上标注 @Encrypt 注解
     *
     * @return
     */
    @Encrypt
    @GetMapping("/encryptTest")
    public String encryptTest() {
        return "高并发 & 微服务 & 性能调优实战案例 100 讲";
    }

    /**
     * 请求和返回值同时加密测试，需要在方法上标注2个注解：@Decrypt、@Encrypt
     *
     * @return
     */
    @Decrypt
    @Encrypt
    @PostMapping("/secureTest")
    public List<String> secureTest(@RequestBody List<String> body) {
        log.info("请求和返回值同时加密测试，请求参数：{}", body);
        return body;
    }


    @Autowired
    private AES aes;

    /**
     * 加密：传入明文，返回密文
     *
     * @param body 明文
     * @return 密文
     */
    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String body) {
        return aes.encryptHex(body);
    }

    /**
     * 解密：传入密文，返回明文
     *
     * @param body 密文
     * @return
     */
    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String body) {
        return aes.decryptStr(body);
    }
}
